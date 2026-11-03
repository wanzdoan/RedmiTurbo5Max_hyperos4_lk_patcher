
void FUN_ffff0000b803ea44(void)

{
  if ((DAT_ffff0000b81282dc & 1) == 0) {
    FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
    DAT_ffff0000b81282dc = 1;
  }
  FUN_ffff0000b803def4(DAT_ffff0000b81298b0);
  return;
}


