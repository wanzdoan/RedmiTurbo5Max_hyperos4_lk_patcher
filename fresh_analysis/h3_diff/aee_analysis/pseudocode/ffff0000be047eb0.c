
undefined4 FUN_ffff0000be047eb0(undefined8 param_1)

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
  
  FUN_ffff0000be041b94();
  if ((DAT_ffff0000be124adc & 1) == 0) {
    FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
    DAT_ffff0000be124adc = 1;
  }
  if (DAT_ffff0000be1260b0 == 0) {
    lVar6 = FUN_ffff0000be046d24(0,param_1);
    if ((DAT_ffff0000be124adc & 1) == 0) {
      FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
      DAT_ffff0000be124adc = 1;
    }
    DAT_ffff0000be1260b0 = lVar6;
    if (lVar6 == 0) {
      return 3;
    }
  }
  piVar7 = (int *)FUN_ffff0000be047288(DAT_ffff0000be1260b0);
  if (piVar7 == (int *)0x0) {
    return 3;
  }
  FUN_ffff0000be060ae0(auStack_9e8,0,0x998);
  if (piVar7[0x98] == 1) {
    FUN_ffff0000be045860(piVar7);
  }
  if (DAT_ffff0000be12608c == 0) {
    FUN_ffff0000be0482c0(param_1);
  }
  if ((DAT_ffff0000be124adc & 1) == 0) {
    FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
    DAT_ffff0000be124adc = 1;
  }
  uVar3 = DAT_ffff0000be1260c8;
  uVar4 = FUN_ffff0000be047450(DAT_ffff0000be1260b0);
  FUN_ffff0000be040218(uVar3,uVar4);
  if ((DAT_ffff0000be124adc & 1) == 0) {
    FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
    DAT_ffff0000be124adc = 1;
  }
  FUN_ffff0000be040950(DAT_ffff0000be1260c8,0,param_1,0);
  FUN_ffff0000be060ae0(auStack_9e8,0,0x998);
  FUN_ffff0000be0607b0(auStack_708,piVar7 + 0x5c,0x5b0);
  if ((DAT_ffff0000be124adc & 1) == 0) {
    FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
    DAT_ffff0000be124adc = 1;
  }
  local_c0 = FUN_ffff0000be0472e8(DAT_ffff0000be1260b0);
  if ((DAT_ffff0000be124adc & 1) == 0) {
    FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
    DAT_ffff0000be124adc = 1;
  }
  local_bc = FUN_ffff0000be047328(DAT_ffff0000be1260b0);
  if (*piVar7 == 1) {
    uVar2 = piVar7[0x4e];
  }
  else {
    if (*piVar7 != 2) goto LAB_ffff0000be0480e0;
    uVar2 = piVar7[0x65];
  }
  if (uVar2 < 3) {
    local_c4 = *(undefined4 *)(&DAT_ffff0000be0cc3d0 + (long)(int)uVar2 * 4);
  }
LAB_ffff0000be0480e0:
  local_9dc = 1;
  if ((DAT_ffff0000be124adc & 1) == 0) {
    FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
    DAT_ffff0000be124adc = 1;
  }
  pcVar8 = *(code **)(*(long *)(DAT_ffff0000be1260b0 + 8) + 0x50);
  if ((pcVar8 != (code *)0x0) && ((*pcVar8)(param_1), (DAT_ffff0000be124adc & 1) == 0)) {
    FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
    DAT_ffff0000be124adc = 1;
  }
  FUN_ffff0000be04125c(DAT_ffff0000be1260c8,auStack_9e8,0);
  if ((DAT_ffff0000be124adc & 1) == 0) {
    FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
    DAT_ffff0000be124adc = 1;
  }
  uVar4 = FUN_ffff0000be04716c(DAT_ffff0000be1260b0,param_1);
  if ((DAT_ffff0000be124adc & 1) == 0) {
    FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
    DAT_ffff0000be124adc = 1;
  }
  iVar5 = FUN_ffff0000be047450(DAT_ffff0000be1260b0);
  if (iVar5 != 0) {
    uVar1 = 0x2b0010;
    if (piVar7[0x1ae] != 1) {
      uVar1 = 0xa0004;
    }
    if ((DAT_ffff0000be124adc & 1) == 0) {
      FUN_ffff0000be060ae0(0xa0004,&DAT_ffff0000be1260a0,0,0x38);
      DAT_ffff0000be124adc = 1;
    }
    FUN_ffff0000be04193c(DAT_ffff0000be1260c8,7,uVar1);
  }
  if ((DAT_ffff0000be124adc & 1) == 0) {
    FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
    DAT_ffff0000be124adc = 1;
  }
  FUN_ffff0000be0418dc(DAT_ffff0000be1260c8,7);
  if ((DAT_ffff0000be124adc & 1) == 0) {
    FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
    DAT_ffff0000be124adc = 1;
  }
  FUN_ffff0000be0418dc(DAT_ffff0000be1260c8,1);
  if ((DAT_ffff0000be124adc & 1) == 0) {
    FUN_ffff0000be060ae0(&DAT_ffff0000be1260a0,0,0x38);
    DAT_ffff0000be124adc = 1;
  }
  DAT_ffff0000be126090 = 1;
  DAT_ffff0000be1260a0 = 1;
  return uVar4;
}


