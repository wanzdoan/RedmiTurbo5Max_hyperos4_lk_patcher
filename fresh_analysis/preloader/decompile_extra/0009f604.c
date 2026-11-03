// File offset entry: 0x9f604

undefined8 FUN_0209f504(long param_1,uint param_2)

{
  uint uVar1;
  undefined8 uVar2;
  
  uVar1 = 1 << (ulong)(param_2 & 0x1f);
  FUN_0208ada0(0x20cee15,uVar1,*(undefined4 *)(param_1 + 0x24));
  if ((*(uint *)(param_1 + 0x24) & uVar1) == 0) {
    FUN_0208ada0(0x20b510e,param_2);
    uVar2 = 0x10200e;
  }
  else {
    uVar2 = 0;
  }
  return uVar2;
}


