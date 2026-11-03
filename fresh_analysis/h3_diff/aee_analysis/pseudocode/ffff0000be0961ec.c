
uint FUN_ffff0000be0961ec(long param_1,int param_2,int param_3)

{
  undefined5 uVar1;
  uint uVar2;
  uint uVar3;
  byte bVar4;
  undefined8 local_48;
  undefined1 local_40;
  undefined4 uStack_3f;
  undefined3 local_3b;
  undefined2 uStack_38;
  undefined3 uStack_36;
  
  FUN_ffff0000be05fd98(&DAT_ffff0000be0b835e,s__gate_ic_i2c_backlight_enable_ffff0000be0b9561);
  if (param_1 == 0) {
    FUN_ffff0000be05fd98(s_fdt_is_NULL__ffff0000be0c3960);
    uVar3 = 0xffffffff;
  }
  else if (param_2 == 0) {
    FUN_ffff0000be095ea4(param_1,8,0);
    uVar3 = 0;
  }
  else {
    if (param_3 == 0) {
      bVar4 = 0x68;
    }
    else {
      bVar4 = 0x6b;
      DAT_ffff0000be1b7dbc = 0x7ff;
    }
    uStack_3f = 0;
    local_48._0_4_ = (uint)bVar4 << 0x10;
    uVar1 = CONCAT14((char)DAT_ffff0000be1b7dbc,(int)local_48);
    local_3b = 0;
    uStack_36 = 0;
    local_48._0_4_ = CONCAT13(0x9d,(int3)(int)local_48);
    local_48 = (ulong)(CONCAT24((short)(CONCAT15((char)(DAT_ffff0000be1b7dbc >> 3),uVar1) >> 0x20),
                                (int)local_48) & 0xff07ffffffff);
    local_40 = 0x15;
    uStack_38 = 0xb706;
    uVar2 = FUN_ffff0000be0960b0(param_1,2,(ulong)&local_48 | 2,7);
    uVar3 = FUN_ffff0000be0960b0(param_1,0x10,&uStack_38,2);
    uVar3 = uVar3 | uVar2;
  }
  return uVar3;
}


