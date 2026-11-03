
int FUN_ffff0000be09ab0c(long param_1,undefined8 param_2,undefined8 param_3,undefined4 *param_4)

{
  undefined4 uVar1;
  int iVar2;
  
  thunk_FUN_ffff0000be060ae0(param_4,0,0x40);
  uVar1 = FUN_ffff0000be0965a4(0x90790922);
  *param_4 = uVar1;
  uVar1 = FUN_ffff0000be0965a4(1);
  param_4[1] = uVar1;
  uVar1 = FUN_ffff0000be0965a4(0);
  param_4[2] = uVar1;
  iVar2 = (**(code **)(param_1 + 0x28))(param_1,param_2,param_3,0x40,param_4);
  if (iVar2 != 0) {
    FUN_ffff0000be05fd98(s__AVB__Fail_to_write_init_persist_ffff0000be0c5cec,param_2);
  }
  return iVar2;
}


