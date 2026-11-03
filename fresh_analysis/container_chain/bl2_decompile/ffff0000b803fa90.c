
undefined8 FUN_ffff0000b803fa90(undefined4 param_1)

{
  int iVar1;
  
  if ((DAT_ffff0000b81282dc & 1) == 0) {
    FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
    DAT_ffff0000b81282dc = 1;
  }
  if (DAT_ffff0000b81298c8 != 0) {
    iVar1 = FUN_ffff0000b80383b0();
    if (iVar1 != 0) {
      if ((DAT_ffff0000b81282dc & 1) == 0) {
        FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
        DAT_ffff0000b81282dc = 1;
      }
      FUN_ffff0000b8038730(DAT_ffff0000b81298c8,1,100);
    }
    if ((DAT_ffff0000b81282dc & 1) == 0) {
      FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
      DAT_ffff0000b81282dc = 1;
    }
    FUN_ffff0000b803e170(DAT_ffff0000b81298b0,param_1);
  }
  return 0;
}


