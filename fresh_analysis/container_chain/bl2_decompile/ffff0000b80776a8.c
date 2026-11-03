
ulong FUN_ffff0000b80776a8(long param_1,uint param_2)

{
  uint uVar1;
  uint uVar2;
  ulong uVar3;
  ulong local_48;
  undefined1 local_40;
  undefined4 uStack_3f;
  undefined3 local_3b;
  undefined2 uStack_38;
  undefined3 uStack_36;
  
  FUN_ffff0000b804c33c(&DAT_ffff0000b8095dc6,s__gate_ic_i2c_backight_level_set_ffff0000b8094ff7);
  if (param_1 == 0) {
    FUN_ffff0000b804c33c(s_fdt_is_NULL__ffff0000b809f0d6);
    uVar3 = 0xffffffff;
  }
  else if ((param_2 == 0) || (DAT_ffff0000b81b9ae4 != 0)) {
    local_48 = CONCAT62(local_48._2_6_,CONCAT11((char)(param_2 >> 3),(char)param_2)) &
               0xffffffffffffff07;
    DAT_ffff0000b81b9ae4 = param_2;
    uVar3 = FUN_ffff0000b8077454(param_1,4,&local_48,2);
  }
  else {
    DAT_ffff0000b81b9ae4 = param_2;
    FUN_ffff0000b804c33c(&DAT_ffff0000b8095dc6,s__gate_ic_i2c_backlight_enable_ffff0000b8096cc7);
    uStack_3f = 0;
    local_3b = 0;
    uStack_36 = 0;
    local_48 = (ulong)(CONCAT15((char)(DAT_ffff0000b81b9ae4 >> 3),
                                CONCAT14((char)DAT_ffff0000b81b9ae4,0x9d680000)) & 0xff07ffffffff);
    local_40 = 0x15;
    uStack_38 = 0xb706;
    uVar1 = FUN_ffff0000b8077454(param_1,2,(ulong)&local_48 | 2,7);
    uVar2 = FUN_ffff0000b8077454(param_1,0x10,&uStack_38,2);
    uVar3 = (ulong)(uVar2 | uVar1);
  }
  return uVar3;
}


