
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

char * FUN_ffff0000be03572c(void)

{
  uint uVar1;
  
  uVar1 = (_DAT_ffff00001c010024 >> 0x1a & 7) - 1;
  if (uVar1 < 7) {
    return &DAT_ffff0000be0c9578 + *(int *)(&DAT_ffff0000be0c9578 + (long)(int)uVar1 * 4);
  }
  return s_rst_from__unknown_ffff0000be0b2062;
}


