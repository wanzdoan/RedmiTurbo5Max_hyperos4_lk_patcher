
void FUN_ffff0000be009030(undefined8 param_1)

{
  undefined4 local_50;
  undefined8 local_4c;
  undefined8 local_44;
  undefined8 local_3c;
  undefined4 local_34;
  
  local_34 = 0;
  local_3c = 0;
  local_44 = 0;
  local_4c = 0;
  local_50 = 0x78563412;
  FUN_ffff0000be029a00(&local_50);
  FUN_ffff0000be029a00(&local_4c);
  FUN_ffff0000be029a00((long)&local_4c + 4);
  FUN_ffff0000be029a00(&local_44);
  FUN_ffff0000be029a00((long)&local_44 + 4);
  FUN_ffff0000be029a00(&local_3c);
  FUN_ffff0000be029a00((long)&local_3c + 4);
  FUN_ffff0000be029a00(&local_34);
  FUN_ffff0000be05fd98
            (s__s__d__Use_random_key_ffff0000be0a687e,s_mrdump_gen_key_ffff0000be0a6895,0x24);
  FUN_ffff0000be0607b0(param_1,&local_50,0x20);
  return;
}


