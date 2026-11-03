
undefined8 FUN_ffff0000b803f0fc(void)

{
  undefined8 uVar1;
  
  if ((DAT_ffff0000b8129890 & 1) == 0) {
    FUN_ffff0000b804c33c
              (s_skip__s_before_init_ffff0000b808af52,s_primary_display_suspend_ffff0000b8092bd6);
    uVar1 = 0xffffffff;
  }
  else {
    if ((DAT_ffff0000b81282dc & 1) == 0) {
      FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
      DAT_ffff0000b81282dc = 1;
    }
    FUN_ffff0000b803e118(DAT_ffff0000b81298b0);
    if ((DAT_ffff0000b81282dc & 1) == 0) {
      FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
      DAT_ffff0000b81282dc = 1;
    }
    FUN_ffff0000b8037b4c(DAT_ffff0000b81298c8,0);
    if ((DAT_ffff0000b81282dc & 1) == 0) {
      FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
      DAT_ffff0000b81282dc = 1;
    }
    FUN_ffff0000b8038260(DAT_ffff0000b81298c8,0);
    if ((DAT_ffff0000b81282dc & 1) == 0) {
      FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
      DAT_ffff0000b81282dc = 1;
    }
    uVar1 = 0;
    DAT_ffff0000b81298a0 = 0;
  }
  return uVar1;
}


