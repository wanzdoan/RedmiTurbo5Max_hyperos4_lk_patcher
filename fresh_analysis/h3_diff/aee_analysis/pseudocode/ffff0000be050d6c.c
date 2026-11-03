
void FUN_ffff0000be050d6c(undefined8 param_1)

{
  int iVar1;
  undefined8 uVar2;
  
  iVar1 = FUN_ffff0000be060f54(param_1,&DAT_ffff0000be0a8df2,3);
  if (iVar1 == 0) {
    FUN_ffff0000be05fd98(s__FASTBOOT__oem_printk_ratelimit_o_ffff0000be0abd03);
    uVar2 = 1;
  }
  else {
    iVar1 = FUN_ffff0000be060f54(param_1,&DAT_ffff0000be0b8640,4);
    if (iVar1 != 0) {
      FUN_ffff0000be013738(s_UNKNOWN_ARGUMENT_ffff0000be0c585d);
      return;
    }
    FUN_ffff0000be05fd98(s__FASTBOOT__oem_printk_ratelimit_o_ffff0000be0bd870);
    uVar2 = 0;
  }
  FUN_ffff0000be0248f4(2,uVar2);
  FUN_ffff0000be013828(&DAT_ffff0000be0ae340);
  return;
}


