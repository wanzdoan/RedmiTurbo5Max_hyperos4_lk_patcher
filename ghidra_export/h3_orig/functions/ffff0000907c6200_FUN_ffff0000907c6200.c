// ffff0000907c6200  size=160  FUN_ffff0000907c6200

undefined8 FUN_ffff0000907c6200(undefined8 param_1,uint *param_2)

{
  undefined4 uVar1;
  undefined8 uVar2;
  undefined8 local_60;
  ulong local_58;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined8 local_40;
  undefined8 uStack_38;
  undefined8 local_30;
  undefined8 uStack_28;
  
  if (param_2 == (uint *)0x0) {
    uVar2 = 0x100006;
  }
  else {
    local_30 = 0;
    uStack_28 = 0;
    local_40 = 0;
    uStack_38 = 0;
    local_50 = 0;
    uStack_48 = 0;
    local_60 = 0;
    local_58 = 0;
    thunk_FUN_ffff00009077e858(&local_60,param_1,0xc);
    uVar1 = FUN_ffff0000907c09dc(local_60 & 0xffffffff);
    local_60 = CONCAT44(local_60._4_4_,uVar1);
    uVar1 = FUN_ffff0000907c09dc(local_60._4_4_);
    local_60 = CONCAT44(uVar1,(int)local_60);
    FUN_ffff0000907c09dc(local_58 & 0xffffffff);
    uVar2 = 0;
    *param_2 = (uint)((int)local_60 == -0x6f86f6de);
  }
  return uVar2;
}


