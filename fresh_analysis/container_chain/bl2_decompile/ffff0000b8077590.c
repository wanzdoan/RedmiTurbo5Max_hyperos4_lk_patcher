
uint FUN_ffff0000b8077590(long param_1,int param_2,int param_3)

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
  
  FUN_ffff0000b804c33c(&DAT_ffff0000b8095dc6,s__gate_ic_i2c_backlight_enable_ffff0000b8096cc7);
  if (param_1 == 0) {
    FUN_ffff0000b804c33c(s_fdt_is_NULL__ffff0000b809f0d6);
    uVar3 = 0xffffffff;
  }
  else if (param_2 == 0) {
    FUN_ffff0000b8077248(param_1,8,0);
    uVar3 = 0;
  }
  else {
    if (param_3 == 0) {
      bVar4 = 0x68;
    }
    else {
      bVar4 = 0x6b;
      DAT_ffff0000b81b9ae4 = 0x7ff;
    }
    uStack_3f = 0;
    local_48._0_4_ = (uint)bVar4 << 0x10;
    uVar1 = CONCAT14((char)DAT_ffff0000b81b9ae4,(int)local_48);
    local_3b = 0;
    uStack_36 = 0;
    local_48._0_4_ = CONCAT13(0x9d,(int3)(int)local_48);
    local_48 = (ulong)(CONCAT24((short)(CONCAT15((char)(DAT_ffff0000b81b9ae4 >> 3),uVar1) >> 0x20),
                                (int)local_48) & 0xff07ffffffff);
    local_40 = 0x15;
    uStack_38 = 0xb706;
    uVar2 = FUN_ffff0000b8077454(param_1,2,(ulong)&local_48 | 2,7);
    uVar3 = FUN_ffff0000b8077454(param_1,0x10,&uStack_38,2);
    uVar3 = uVar3 | uVar2;
  }
  return uVar3;
}


