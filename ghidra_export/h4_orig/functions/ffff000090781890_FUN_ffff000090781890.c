// ffff000090781890  size=128  FUN_ffff000090781890

undefined8 FUN_ffff000090781890(ulong param_1,uint param_2,long param_3)

{
  undefined4 uVar1;
  long lVar2;
  undefined8 uVar3;
  
  uVar3 = FUN_ffff00009077f348
                    (&DAT_ffff00009087cfe0 +
                     (ulong)param_2 * 0x28 + (param_1 & 0xffffffff) * 0x80 + 0x10,param_3,0x14);
  if ((int)uVar3 == 0) {
    uVar1 = *(undefined4 *)(param_3 + 0x20);
    lVar2 = (ulong)param_2 * 0x28 + (param_1 & 0xffffffff) * 0x80;
    *(undefined8 *)(&DAT_ffff00009087cfe0 + lVar2 + 0x28) = *(undefined8 *)(param_3 + 0x18);
    *(undefined4 *)(&DAT_ffff00009087cfe0 + lVar2 + 0x30) = uVar1;
  }
  else {
    uVar3 = 0xffffffff;
  }
  return uVar3;
}


