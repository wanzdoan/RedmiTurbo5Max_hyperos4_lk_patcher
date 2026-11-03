// ffff000090767a78  size=160  FUN_ffff000090767a78

void FUN_ffff000090767a78(undefined8 param_1)

{
  int iVar1;
  undefined8 uVar2;
  
  iVar1 = FUN_ffff00009077f348(param_1,&DAT_ffff0000907d7365,3);
  if (iVar1 == 0) {
    FUN_ffff00009077e11c(s__FASTBOOT__oem_printk_ratelimit_o_ffff0000907db6b3);
    uVar2 = 1;
  }
  else {
    iVar1 = FUN_ffff00009077f348(param_1,&DAT_ffff0000907edab6,4);
    if (iVar1 != 0) {
      FUN_ffff000090709bec(s_UNKNOWN_ARGUMENT_ffff0000908015d4);
      return;
    }
    FUN_ffff00009077e11c(s__FASTBOOT__oem_printk_ratelimit_o_ffff0000907f536b);
    uVar2 = 0;
  }
  FUN_ffff00009072af40(2,uVar2);
  FUN_ffff000090709cdc(&DAT_ffff0000907df338);
  return;
}


