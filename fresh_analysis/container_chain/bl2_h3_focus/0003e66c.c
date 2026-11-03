// File offset entry: 0x3e66c

undefined4 FUN_ffff0000b803e66c(undefined8 param_1)

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
  
  FUN_ffff0000b8038344();
  if ((DAT_ffff0000b812727c & 1) == 0) {
    FUN_ffff0000b804cbe4(&DAT_ffff0000b8128840,0,0x38);
    DAT_ffff0000b812727c = 1;
  }
  if (DAT_ffff0000b8128850 == 0) {
    lVar6 = FUN_ffff0000b803d4d4(0,param_1);
    if ((DAT_ffff0000b812727c & 1) == 0) {
      FUN_ffff0000b804cbe4(&DAT_ffff0000b8128840,0,0x38);
      DAT_ffff0000b812727c = 1;
    }
    DAT_ffff0000b8128850 = lVar6;
    if (lVar6 == 0) {
      return 3;
    }
  }
  piVar7 = (int *)FUN_ffff0000b803da38(DAT_ffff0000b8128850);
  if (piVar7 == (int *)0x0) {
    return 3;
  }
  FUN_ffff0000b804cbe4(auStack_9e8,0,0x998);
  if (piVar7[0x98] == 1) {
    FUN_ffff0000b803c010(piVar7);
  }
  if (DAT_ffff0000b812882c == 0) {
    FUN_ffff0000b803ea7c(param_1);
  }
  if ((DAT_ffff0000b812727c & 1) == 0) {
    FUN_ffff0000b804cbe4(&DAT_ffff0000b8128840,0,0x38);
    DAT_ffff0000b812727c = 1;
  }
  uVar3 = DAT_ffff0000b8128868;
  uVar4 = FUN_ffff0000b803dc00(DAT_ffff0000b8128850);
  FUN_ffff0000b80369c8(uVar3,uVar4);
  if ((DAT_ffff0000b812727c & 1) == 0) {
    FUN_ffff0000b804cbe4(&DAT_ffff0000b8128840,0,0x38);
    DAT_ffff0000b812727c = 1;
  }
  FUN_ffff0000b8037100(DAT_ffff0000b8128868,0,param_1,0);
  FUN_ffff0000b804cbe4(auStack_9e8,0,0x998);
  FUN_ffff0000b804c8b4(auStack_708,piVar7 + 0x5c,0x5b0);
  if ((DAT_ffff0000b812727c & 1) == 0) {
    FUN_ffff0000b804cbe4(&DAT_ffff0000b8128840,0,0x38);
    DAT_ffff0000b812727c = 1;
  }
  local_c0 = FUN_ffff0000b803da98(DAT_ffff0000b8128850);
  if ((DAT_ffff0000b812727c & 1) == 0) {
    FUN_ffff0000b804cbe4(&DAT_ffff0000b8128840,0,0x38);
    DAT_ffff0000b812727c = 1;
  }
  local_bc = FUN_ffff0000b803dad8(DAT_ffff0000b8128850);
  if (*piVar7 == 1) {
    uVar2 = piVar7[0x4e];
  }
  else {
    if (*piVar7 != 2) goto LAB_ffff0000b803e89c;
    uVar2 = piVar7[0x65];
  }
  if (uVar2 < 3) {
    local_c4 = *(undefined4 *)(&DAT_ffff0000b80a5af8 + (long)(int)uVar2 * 4);
  }
LAB_ffff0000b803e89c:
  local_9dc = 1;
  if ((DAT_ffff0000b812727c & 1) == 0) {
    FUN_ffff0000b804cbe4(&DAT_ffff0000b8128840,0,0x38);
    DAT_ffff0000b812727c = 1;
  }
  pcVar8 = *(code **)(*(long *)(DAT_ffff0000b8128850 + 8) + 0x50);
  if ((pcVar8 != (code *)0x0) && ((*pcVar8)(param_1), (DAT_ffff0000b812727c & 1) == 0)) {
    FUN_ffff0000b804cbe4(&DAT_ffff0000b8128840,0,0x38);
    DAT_ffff0000b812727c = 1;
  }
  FUN_ffff0000b8037a0c(DAT_ffff0000b8128868,auStack_9e8,0);
  if ((DAT_ffff0000b812727c & 1) == 0) {
    FUN_ffff0000b804cbe4(&DAT_ffff0000b8128840,0,0x38);
    DAT_ffff0000b812727c = 1;
  }
  uVar4 = FUN_ffff0000b803d91c(DAT_ffff0000b8128850,param_1);
  if ((DAT_ffff0000b812727c & 1) == 0) {
    FUN_ffff0000b804cbe4(&DAT_ffff0000b8128840,0,0x38);
    DAT_ffff0000b812727c = 1;
  }
  iVar5 = FUN_ffff0000b803dc00(DAT_ffff0000b8128850);
  if (iVar5 != 0) {
    uVar1 = 0x2b0010;
    if (piVar7[0x1ae] != 1) {
      uVar1 = 0xa0004;
    }
    if ((DAT_ffff0000b812727c & 1) == 0) {
      FUN_ffff0000b804cbe4(0xa0004,&DAT_ffff0000b8128840,0,0x38);
      DAT_ffff0000b812727c = 1;
    }
    FUN_ffff0000b80380ec(DAT_ffff0000b8128868,7,uVar1);
  }
  if ((DAT_ffff0000b812727c & 1) == 0) {
    FUN_ffff0000b804cbe4(&DAT_ffff0000b8128840,0,0x38);
    DAT_ffff0000b812727c = 1;
  }
  FUN_ffff0000b803808c(DAT_ffff0000b8128868,7);
  if ((DAT_ffff0000b812727c & 1) == 0) {
    FUN_ffff0000b804cbe4(&DAT_ffff0000b8128840,0,0x38);
    DAT_ffff0000b812727c = 1;
  }
  FUN_ffff0000b803808c(DAT_ffff0000b8128868,1);
  if ((DAT_ffff0000b812727c & 1) == 0) {
    FUN_ffff0000b804cbe4(&DAT_ffff0000b8128840,0,0x38);
    DAT_ffff0000b812727c = 1;
  }
  DAT_ffff0000b8128830 = 1;
  DAT_ffff0000b8128840 = 1;
  return uVar4;
}


