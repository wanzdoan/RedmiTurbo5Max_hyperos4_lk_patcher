
undefined8 FUN_ffff000090781890(ulong param_1,uint param_2,long param_3)

{
  undefined4 uVar1;
  undefined8 uVar2;
  
  param_1 = param_1 & 0xffffffff;
  uVar2 = FUN_ffff00009077f348
                    (&DAT_ffff00009087cfe0 + param_1 * 0x20 + (ulong)param_2 * 10 + 4,param_3,0x14);
  if ((int)uVar2 == 0) {
    uVar1 = *(undefined4 *)(param_3 + 0x20);
    *(undefined8 *)(&DAT_ffff00009087cfe0 + param_1 * 0x20 + (ulong)param_2 * 10 + 10) =
         *(undefined8 *)(param_3 + 0x18);
    (&DAT_ffff00009087cfe0)[param_1 * 0x20 + (ulong)param_2 * 10 + 0xc] = uVar1;
  }
  else {
    uVar2 = 0xffffffff;
  }
  return uVar2;
}


