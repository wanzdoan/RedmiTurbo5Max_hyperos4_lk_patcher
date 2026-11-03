
undefined4 FUN_ffff0000b803eaf8(undefined8 param_1)

{
  undefined4 uVar1;
  uint uVar2;
  undefined8 uVar3;
  undefined4 uVar4;
  int iVar5;
  long lVar6;
  int *piVar7;
  code *pcVar8;
  undefined1 auStack_9e8 [12];
  undefined4 local_9dc;
  undefined1 auStack_708 [1604];
  undefined4 local_c4;
  undefined4 local_c0;
  undefined4 local_bc;
  
  FUN_ffff0000b80387d0();
  if ((DAT_ffff0000b81282dc & 1) == 0) {
    FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
    DAT_ffff0000b81282dc = 1;
  }
  if (DAT_ffff0000b81298b0 == 0) {
    lVar6 = FUN_ffff0000b803d960(0,param_1);
    if ((DAT_ffff0000b81282dc & 1) == 0) {
      FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
      DAT_ffff0000b81282dc = 1;
    }
    DAT_ffff0000b81298b0 = lVar6;
    if (lVar6 == 0) {
      return 3;
    }
  }
  piVar7 = (int *)FUN_ffff0000b803dec4(DAT_ffff0000b81298b0);
  if (piVar7 == (int *)0x0) {
    return 3;
  }
  FUN_ffff0000b804d070(auStack_9e8,0,0x998);
  if (piVar7[0x98] == 1) {
    FUN_ffff0000b803c49c(piVar7);
  }
  if (DAT_ffff0000b812988c == 0) {
    FUN_ffff0000b803ef08(param_1);
  }
  if ((DAT_ffff0000b81282dc & 1) == 0) {
    FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
    DAT_ffff0000b81282dc = 1;
  }
  uVar3 = DAT_ffff0000b81298c8;
  uVar4 = FUN_ffff0000b803e08c(DAT_ffff0000b81298b0);
  FUN_ffff0000b8036e54(uVar3,uVar4);
  if ((DAT_ffff0000b81282dc & 1) == 0) {
    FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
    DAT_ffff0000b81282dc = 1;
  }
  FUN_ffff0000b803758c(DAT_ffff0000b81298c8,0,param_1,0);
  FUN_ffff0000b804d070(auStack_9e8,0,0x998);
  FUN_ffff0000b804cd40(auStack_708,piVar7 + 0x5c,0x5b0);
  if ((DAT_ffff0000b81282dc & 1) == 0) {
    FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
    DAT_ffff0000b81282dc = 1;
  }
  local_c0 = FUN_ffff0000b803df24(DAT_ffff0000b81298b0);
  if ((DAT_ffff0000b81282dc & 1) == 0) {
    FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
    DAT_ffff0000b81282dc = 1;
  }
  local_bc = FUN_ffff0000b803df64(DAT_ffff0000b81298b0);
  if (*piVar7 == 1) {
    uVar2 = piVar7[0x4e];
  }
  else {
    if (*piVar7 != 2) goto LAB_ffff0000b803ed28;
    uVar2 = piVar7[0x65];
  }
  if (uVar2 < 3) {
    local_c4 = *(undefined4 *)(&DAT_ffff0000b80a6c40 + (long)(int)uVar2 * 4);
  }
LAB_ffff0000b803ed28:
  local_9dc = 1;
  if ((DAT_ffff0000b81282dc & 1) == 0) {
    FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
    DAT_ffff0000b81282dc = 1;
  }
  pcVar8 = *(code **)(*(long *)(DAT_ffff0000b81298b0 + 8) + 0x50);
  if ((pcVar8 != (code *)0x0) && ((*pcVar8)(param_1), (DAT_ffff0000b81282dc & 1) == 0)) {
    FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
    DAT_ffff0000b81282dc = 1;
  }
  FUN_ffff0000b8037e98(DAT_ffff0000b81298c8,auStack_9e8,0);
  if ((DAT_ffff0000b81282dc & 1) == 0) {
    FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
    DAT_ffff0000b81282dc = 1;
  }
  uVar4 = FUN_ffff0000b803dda8(DAT_ffff0000b81298b0,param_1);
  if ((DAT_ffff0000b81282dc & 1) == 0) {
    FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
    DAT_ffff0000b81282dc = 1;
  }
  iVar5 = FUN_ffff0000b803e08c(DAT_ffff0000b81298b0);
  if (iVar5 != 0) {
    uVar1 = 0x2b0010;
    if (piVar7[0x1ae] != 1) {
      uVar1 = 0xa0004;
    }
    if ((DAT_ffff0000b81282dc & 1) == 0) {
      FUN_ffff0000b804d070(0xa0004,&DAT_ffff0000b81298a0,0,0x38);
      DAT_ffff0000b81282dc = 1;
    }
    FUN_ffff0000b8038578(DAT_ffff0000b81298c8,7,uVar1);
  }
  if ((DAT_ffff0000b81282dc & 1) == 0) {
    FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
    DAT_ffff0000b81282dc = 1;
  }
  FUN_ffff0000b8038518(DAT_ffff0000b81298c8,7);
  if ((DAT_ffff0000b81282dc & 1) == 0) {
    FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
    DAT_ffff0000b81282dc = 1;
  }
  FUN_ffff0000b8038518(DAT_ffff0000b81298c8,1);
  if ((DAT_ffff0000b81282dc & 1) == 0) {
    FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
    DAT_ffff0000b81282dc = 1;
  }
  DAT_ffff0000b8129890 = 1;
  DAT_ffff0000b81298a0 = 1;
  return uVar4;
}


