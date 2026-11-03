// ffff00009077ddac  size=168  FUN_ffff00009077ddac

undefined4 FUN_ffff00009077ddac(undefined8 *param_1,undefined8 param_2)

{
  undefined4 uVar1;
  undefined1 *local_40;
  undefined1 *puStack_38;
  undefined8 local_30;
  undefined8 uStack_28;
  
  FUN_ffff0000907c2f94(*param_1);
  local_30 = 0;
  uStack_28 = 0xffffffd0;
  local_40 = (undefined1 *)register0x00000008;
  puStack_38 = &stack0xffffffffffffff80;
  uVar1 = FUN_ffff00009077fd88(FUN_ffff00009077dd80,param_1,param_2,&local_40);
  FUN_ffff0000907c2f74(*param_1);
  FUN_ffff0000907c2fb4(*param_1);
  return uVar1;
}


