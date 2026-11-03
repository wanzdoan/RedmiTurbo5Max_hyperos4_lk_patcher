// File offset entry: 0x26b08

void FUN_ffff0000b8026b08(long param_1,ulong param_2)

{
  undefined4 uVar1;
  long local_30;
  long lStack_28;
  undefined4 local_20;
  undefined4 local_1c;
  undefined8 local_18;
  
  lStack_28 = param_1 + 0x280000;
  local_1c = 0xb6db6d;
  if ((param_2 & 1) != 0) {
    local_1c = 0xb6db6a;
  }
  local_20 = 0x11;
  local_18 = 0xb6db69;
  local_30 = param_1;
  uVar1 = FUN_ffff0000b8052bd0(&local_30);
  FUN_ffff0000b804beb0(s_sspm_enable_emimpu_ret__d_ffff0000b809d5e8,uVar1);
  return;
}


