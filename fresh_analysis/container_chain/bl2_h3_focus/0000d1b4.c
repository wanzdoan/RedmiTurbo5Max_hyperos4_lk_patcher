// File offset entry: 0xd1b4

long FUN_ffff0000b800d1b4
               (undefined8 param_1,undefined8 param_2,undefined8 param_3,undefined8 param_4,
               undefined8 param_5,undefined4 param_6,undefined8 param_7)

{
  long lVar1;
  
  FUN_ffff0000b800ad90(&LAB_ffff0000b80acce8,0xffffffff);
  lVar1 = FUN_ffff0000b800caa0(param_1,param_2,param_3,param_4,param_5,param_6,param_7);
  FUN_ffff0000b800ae28(&LAB_ffff0000b80acce8);
  if (lVar1 != 0) {
    return lVar1;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000b804b2b8(s__s_failed_ffff0000b809a472,s_mblock_alloc_range_ffff0000b808cb6f);
}


