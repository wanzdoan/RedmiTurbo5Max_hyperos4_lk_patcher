// ffff0000907677e0  size=160  FUN_ffff0000907677e0

void FUN_ffff0000907677e0(undefined8 param_1)

{
  int iVar1;
  undefined8 uVar2;
  
  iVar1 = FUN_ffff00009077f080(param_1,&DAT_ffff0000907d72cf,3);
  if (iVar1 == 0) {
    FUN_ffff00009077de54(s__FASTBOOT__oem_printk_ratelimit_o_ffff0000907db5a8);
    uVar2 = 1;
  }
  else {
    iVar1 = FUN_ffff00009077f080(param_1,&DAT_ffff0000907ed945,4);
    if (iVar1 != 0) {
      FUN_ffff000090709c60(s_UNKNOWN_ARGUMENT_ffff0000908013f8);
      return;
    }
    FUN_ffff00009077de54(s__FASTBOOT__oem_printk_ratelimit_o_ffff0000907f51fe);
    uVar2 = 0;
  }
  FUN_ffff00009072acfc(2,uVar2);
  FUN_ffff000090709d50(&DAT_ffff0000907df295);
  return;
}


