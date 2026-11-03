
undefined8 FUN_ffff0000be009188(long param_1,long param_2,int param_3)

{
  undefined8 uVar1;
  
  uVar1 = 0xffffffff;
  if (((param_1 != 0) && (param_2 != 0)) &&
     (uVar1 = FUN_ffff0000be054268(param_2,param_3 << 3,param_1 + 0x10), (int)uVar1 != 0)) {
    FUN_ffff0000be05fd98
              (s__s__d__set_aes_key_fail_ffff0000be0bcc83,s_mrdump_set_aes_key_ffff0000be0b4f31,0x52
              );
    uVar1 = 0xffffffff;
  }
  return uVar1;
}


