
ulong FUN_ffff0000be011ce8(long *param_1,uint param_2)

{
  uint uVar1;
  undefined4 uVar2;
  int iVar3;
  ulong uVar4;
  ulong uVar5;
  
  uVar5 = 0;
  do {
    uVar1 = *(uint *)((long)param_1 + (ulong)(param_2 + (int)uVar5) * 4 + 0x2c);
    if (uVar5 != uVar1 - *(int *)((long)param_1 + (ulong)param_2 * 4 + 0x2c)) break;
    FUN_ffff0000be0357ec();
    if (*(char *)((long)param_1 + 0x1102c) != '\0') {
      FUN_ffff0000be0357ec();
      uVar4 = (**(code **)(*param_1 + 0x18))
                        (*param_1,(ulong)uVar1 << 0xc,&DAT_ffff0000be106938,0x1000);
      if ((uVar4 & 1) == 0) {
        FUN_ffff0000be05fd98
                  (s__s__d__Read_BlockData_failed_ffff0000be0aa0a9,
                   s_check_block_valid_ffff0000be0bcd92,0x59);
      }
      else if (DAT_ffff0000be106938 == uVar1) {
        if (DAT_ffff0000be10792c == DAT_ffff0000be106930) {
          uVar2 = FUN_ffff0000be0553a4(0,0,0);
          iVar3 = FUN_ffff0000be0553a4(uVar2,&DAT_ffff0000be106938,0xffc);
          if (DAT_ffff0000be107934 == iVar3) goto LAB_ffff0000be011d40;
          FUN_ffff0000be05fd98
                    (s__s__d___s__Get_BlockData_crc32_e_ffff0000be0ab2f1,
                     s_check_block_valid_ffff0000be0bcd92,0x6d,s_check_block_valid_ffff0000be0bcd92)
          ;
        }
        else {
          FUN_ffff0000be05fd98
                    (s__s__d__Read_BlockData_timestamp_f_ffff0000be0bcda4,
                     s_check_block_valid_ffff0000be0bcd92,100);
        }
      }
      else {
        FUN_ffff0000be05fd98
                  (s__s__d__Read_BlockData_LBA_failed_ffff0000be0a317c,
                   s_check_block_valid_ffff0000be0bcd92,0x5e,DAT_ffff0000be106938,(ulong)uVar1);
      }
      FUN_ffff0000be05fd98
                (s__s__d__BlockData_Verification_fa_ffff0000be0a5702,s_Num_to_Join_ffff0000be0a5729,
                 0x98);
      uVar5 = 0;
      break;
    }
LAB_ffff0000be011d40:
    uVar5 = uVar5 + 1;
  } while (uVar5 != 0x10);
  return uVar5 & 0xffffffff;
}


