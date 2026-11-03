// ffff000090739a18  size=100  FUN_ffff000090739a18

undefined8 FUN_ffff000090739a18(long param_1,int param_2)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  
  uVar1 = 0x6afdffff;
  if (DAT_ffff00009081940c != 1) {
    uVar1 = 0x6af7ffff;
  }
  uVar3 = 0x20000;
  if (DAT_ffff00009081940c != 1) {
    uVar3 = 0x80000;
  }
  uVar2 = 0;
  if (param_2 != 0) {
    uVar2 = uVar3;
  }
  FUN_ffff000090739868
            ((uint *)(param_1 + 0x44),*(uint *)(param_1 + 0x44) & uVar1 | uVar2 | 0x95000000);
  return 0;
}


