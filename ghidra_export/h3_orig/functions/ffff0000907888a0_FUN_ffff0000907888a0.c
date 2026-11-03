// ffff0000907888a0  size=152  FUN_ffff0000907888a0

uint FUN_ffff0000907888a0
               (undefined8 param_1,undefined8 param_2,undefined8 param_3,undefined4 param_4)

{
  uint uVar1;
  long lVar2;
  
  lVar2 = FUN_ffff00009077bd60();
  if ((lVar2 == 0) && (lVar2 = FUN_ffff00009077bc50(param_1), lVar2 == 0)) {
    FUN_ffff00009077de54(s_failed_to_open__s_ffff0000907d51af,param_1);
    uVar1 = 0xffffffea;
  }
  else {
    uVar1 = FUN_ffff00009077be7c(lVar2,param_3,param_2,param_4);
    FUN_ffff00009077bce4(lVar2);
    uVar1 = uVar1 & (int)uVar1 >> 0x1f;
  }
  return uVar1;
}


