// File offset entry: 0x3f36c

undefined4 FUN_ffff0000b803f36c(void)

{
  long *plVar1;
  
  if ((DAT_ffff0000b81282dc & 1) == 0) {
    FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
    DAT_ffff0000b81282dc = 1;
  }
  if (DAT_ffff0000b81298b0 == (long *)0x0) {
    plVar1 = (long *)FUN_ffff0000b803d960(0,0);
    if ((DAT_ffff0000b81282dc & 1) == 0) {
      FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
      DAT_ffff0000b81282dc = 1;
    }
    DAT_ffff0000b81298b0 = plVar1;
    if (plVar1 == (long *)0x0) {
      return 0;
    }
  }
  if (*DAT_ffff0000b81298b0 == 0) {
    return 0;
  }
  return *(undefined4 *)(*DAT_ffff0000b81298b0 + 0x1c);
}


