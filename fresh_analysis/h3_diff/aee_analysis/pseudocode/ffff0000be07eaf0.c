
undefined4 FUN_ffff0000be07eaf0(undefined8 param_1,undefined8 param_2,undefined8 param_3)

{
  undefined4 uVar1;
  
  FUN_ffff0000be060ae0(&DAT_ffff0000be1b5098,0,0x100);
  DAT_ffff0000be1b5098 = 1;
  DAT_ffff0000be1b509a = 1;
  FUN_ffff0000be087d80(&DAT_ffff0000be1b5098,0x100);
  FUN_ffff0000be087d80(param_1,0x100);
  FUN_ffff0000be087d80(param_2,0x100);
  uVar1 = FUN_ffff0000be086028(param_1,&DAT_ffff0000be1b5098,param_2,param_3);
  FUN_ffff0000be087d80(param_2,0x100);
  FUN_ffff0000be087d80(param_3,0x100);
  return uVar1;
}


