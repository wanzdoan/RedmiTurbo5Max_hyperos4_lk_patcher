
undefined8 FUN_ffff0000b803f4dc(void)

{
  undefined4 uVar1;
  undefined4 uVar2;
  undefined4 uVar3;
  undefined4 uVar4;
  int iVar5;
  
  if ((DAT_ffff0000b81282dc & 1) == 0) {
    FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
    DAT_ffff0000b81282dc = 1;
  }
  uVar1 = FUN_ffff0000b803dfa4(DAT_ffff0000b81298b0);
  if ((DAT_ffff0000b81282dc & 1) == 0) {
    FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
    DAT_ffff0000b81282dc = 1;
  }
  uVar2 = FUN_ffff0000b803dfd4(DAT_ffff0000b81298b0);
  if ((DAT_ffff0000b81282dc & 1) == 0) {
    FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
    DAT_ffff0000b81282dc = 1;
  }
  uVar3 = FUN_ffff0000b803df24(DAT_ffff0000b81298b0);
  if ((DAT_ffff0000b81282dc & 1) == 0) {
    FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
    DAT_ffff0000b81282dc = 1;
  }
  uVar4 = FUN_ffff0000b803df64(DAT_ffff0000b81298b0);
  if ((DAT_ffff0000b81282dc & 1) == 0) {
    FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
    DAT_ffff0000b81282dc = 1;
  }
  if (DAT_ffff0000b81298a8 == 0) {
    iVar5 = FUN_ffff0000b80383b0(DAT_ffff0000b81298c8);
    if (iVar5 != 0) {
      if ((DAT_ffff0000b81282dc & 1) == 0) {
        FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
        DAT_ffff0000b81282dc = 1;
      }
      iVar5 = FUN_ffff0000b803e08c(DAT_ffff0000b81298b0);
      if (iVar5 != 0) {
        return 0;
      }
      FUN_ffff0000b802e1d8(0x2b);
    }
    if ((DAT_ffff0000b81282dc & 1) == 0) {
      FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
      DAT_ffff0000b81282dc = 1;
    }
    FUN_ffff0000b803e004(DAT_ffff0000b81298b0,uVar1,uVar2,uVar3,uVar4,0);
    if (DAT_ffff0000b8129888 == 0) {
      if ((DAT_ffff0000b81282dc & 1) == 0) {
        FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
        DAT_ffff0000b81282dc = 1;
      }
      iVar5 = FUN_ffff0000b803e08c(DAT_ffff0000b81298b0);
      if (iVar5 == 0) {
        if ((DAT_ffff0000b81282dc & 1) == 0) {
          FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
          DAT_ffff0000b81282dc = 1;
        }
        if (*(int *)(DAT_ffff0000b81298b0 + 0x1c) != 0) {
          FUN_ffff0000b802e304(0x2b,0);
        }
      }
    }
    if ((DAT_ffff0000b81282dc & 1) == 0) {
      FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
      DAT_ffff0000b81282dc = 1;
    }
    FUN_ffff0000b8037a00(DAT_ffff0000b81298c8,DAT_ffff0000b8129888);
    if (DAT_ffff0000b8129888 == 0) {
      if ((DAT_ffff0000b81282dc & 1) == 0) {
        FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
        DAT_ffff0000b81282dc = 1;
        iVar5 = DAT_ffff0000b8129888;
      }
      else {
        iVar5 = 0;
      }
      FUN_ffff0000b80381a8(DAT_ffff0000b81298c8,0,iVar5);
    }
  }
  if ((DAT_ffff0000b81282dc & 1) == 0) {
    FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
    DAT_ffff0000b81282dc = 1;
  }
  FUN_ffff0000b80385c4(DAT_ffff0000b81298c8);
  return 0;
}


