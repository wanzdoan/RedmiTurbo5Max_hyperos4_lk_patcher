
ulong FUN_ffff00009078745c(long param_1)

{
  uint uVar1;
  ulong uVar2;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined8 local_40;
  undefined8 uStack_38;
  undefined8 local_30;
  undefined8 uStack_28;
  uint local_4;
  
  local_4 = 0;
  local_30 = 0;
  uStack_28 = 0;
  local_40 = 0;
  uStack_38 = 0;
  local_50 = 0;
  uStack_48 = 0;
  local_60 = 0;
  uStack_58 = 0;
  FUN_ffff00009077eb20(&local_4,param_1 + 10,4);
  uVar1 = local_4 + 0xe;
  if (local_4 - 0x7f3 < 0xfffff80f) {
    local_4 = uVar1;
    FUN_ffff00009077e11c(s_Invalid_secid_len__len__d_ffff0000907eee7c);
    uVar2 = 0xf;
  }
  else {
    local_4 = uVar1;
    FUN_ffff00009077eb20(&local_60,param_1 + (ulong)uVar1,0x40);
    uVar2 = FUN_ffff0000907cd558(param_1,local_4,&local_60,0x40);
    if ((int)uVar2 != 0) {
      FUN_ffff00009077e11c(s_read_secid_failed_from_secinfo_p_ffff0000907d38fe,uVar2 & 0xffffffff);
      uVar2 = 8;
    }
  }
  return uVar2;
}


