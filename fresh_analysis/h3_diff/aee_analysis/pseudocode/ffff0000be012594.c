
void FUN_ffff0000be012594(undefined2 *param_1,undefined8 param_2)

{
  undefined4 uVar1;
  undefined4 uVar2;
  undefined4 uVar3;
  undefined2 uVar4;
  undefined8 local_88;
  undefined8 local_28;
  
  FUN_ffff0000be0112d8(0x45,param_2,&stack0xffffffffffffff80);
  uVar1 = *(undefined4 *)(param_1 + 2);
  uVar2 = *(undefined4 *)(param_1 + 4);
  uVar4 = *param_1;
  uVar3 = *(undefined4 *)(param_1 + 0x12);
  local_88 = 0;
  local_28 = 0;
  FUN_ffff0000be0607b0(&stack0xffffffffffffff80,param_1 + 6,8);
  FUN_ffff0000be0607b0(&local_28,param_1 + 10,8);
  FUN_ffff0000be0607b0(&local_88,param_1 + 0xe,8);
  FUN_ffff0000be05fd98
            (s__s__d__pafile_ver__u_info_lba__u_ffff0000be0c4d81,s_dump_paf_info_ffff0000be0a42f2,
             0x127,uVar4,uVar1,uVar2,0);
  FUN_ffff0000be05fd98
            (s__s__d__coredump_size__llu_timest_ffff0000be0b3dd8,s_dump_paf_info_ffff0000be0a42f2,
             0x129,local_28,local_88,uVar3);
  return;
}


