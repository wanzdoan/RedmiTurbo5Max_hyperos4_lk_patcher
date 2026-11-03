
void FUN_ffff0000b804a14c(void)

{
  FUN_ffff0000b804d360(&DAT_ffff0000b8129be0,s_kernel_ffff0000b809fd3e,0x20);
  DAT_ffff0000b8129c08 = 0xffff000000000000;
  DAT_ffff0000b8129c10 = 0x1000000000000;
  DAT_ffff0000b8129c00 = 3;
  DAT_ffff0000b8129c18 = &DAT_ffff0000b8129c18;
  DAT_ffff0000b8129c20 = &DAT_ffff0000b8129c18;
  FUN_ffff0000b8008c6c(&DAT_ffff0000b8129c28,0xffff000000000000,0x1000000000000,3);
  DAT_ffff0000b8129bd8 = PTR_LOOP_ffff0000b8102078;
  PTR_LOOP_ffff0000b8102078 = (undefined *)&DAT_ffff0000b8129bd0;
  DAT_ffff0000b8129bd0 = &PTR_LOOP_ffff0000b8102070;
  *(undefined ****)DAT_ffff0000b8129bd8 = &DAT_ffff0000b8129bd0;
  return;
}


