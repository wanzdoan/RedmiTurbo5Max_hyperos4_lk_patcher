
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009075d054(uint param_1,undefined8 param_2,long param_3)

{
  uint uVar1;
  undefined8 uVar2;
  undefined4 uVar3;
  int iVar4;
  long lVar5;
  int *piVar6;
  code *pcVar7;
  undefined1 auStack_9e8 [736];
  undefined1 auStack_708 [1604];
  undefined4 local_c4;
  undefined4 local_c0;
  undefined4 local_bc;
  
  FUN_ffff000090756bb8();
  if ((DAT_ffff00009087a99c & 1) == 0) {
    FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
    DAT_ffff00009087a99c = 1;
  }
  if (DAT_ffff00009087bf70 == 0) {
    if (param_3 != 0) {
      FUN_ffff00009077e11c
                (s__s__probe_of_lcm__s_ffff0000907f8a85,s_primary_display_sw_init_ffff0000907f33fc,
                 param_3);
    }
    lVar5 = FUN_ffff00009075bc40(param_3,param_2);
    if ((DAT_ffff00009087a99c & 1) == 0) {
      FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
      DAT_ffff00009087a99c = 1;
    }
    DAT_ffff00009087bf70 = lVar5;
    if (lVar5 == 0) {
      return 3;
    }
  }
  piVar6 = (int *)FUN_ffff00009075c088(DAT_ffff00009087bf70);
  if (piVar6 == (int *)0x0) {
    return 3;
  }
  FUN_ffff00009077ee50(auStack_9e8,0,0x998);
  if (piVar6[0x98] == 1) {
    FUN_ffff00009075a884(piVar6);
  }
  if (DAT_ffff00009087bf4c == 0) {
    FUN_ffff00009075cf10(param_2);
  }
  if ((DAT_ffff00009087a99c & 1) == 0) {
    FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
    DAT_ffff00009087a99c = 1;
  }
  uVar2 = DAT_ffff00009087bf88;
  uVar3 = FUN_ffff00009075c250(DAT_ffff00009087bf70);
  FUN_ffff000090754fe4(uVar2,uVar3);
  if ((DAT_ffff00009087a99c & 1) == 0) {
    FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
    DAT_ffff00009087a99c = 1;
  }
  FUN_ffff00009075571c(DAT_ffff00009087bf88,0,param_2,0);
  FUN_ffff00009077ee50(auStack_9e8,0,0x998);
  FUN_ffff00009077eb20(auStack_708,piVar6 + 0x5c,0x5b0);
  if ((DAT_ffff00009087a99c & 1) == 0) {
    FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
    DAT_ffff00009087a99c = 1;
  }
  local_c0 = FUN_ffff00009075c0e8(DAT_ffff00009087bf70);
  if ((DAT_ffff00009087a99c & 1) == 0) {
    FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
    DAT_ffff00009087a99c = 1;
  }
  local_bc = FUN_ffff00009075c128(DAT_ffff00009087bf70);
  if (*piVar6 == 1) {
    uVar1 = piVar6[0x4e];
  }
  else {
    if (*piVar6 != 2) goto LAB_ffff00009075d2a8;
    uVar1 = piVar6[0x65];
  }
  if (uVar1 < 3) {
    local_c4 = *(undefined4 *)(&DAT_ffff00009080e498 + (long)(int)uVar1 * 4);
  }
LAB_ffff00009075d2a8:
  if ((DAT_ffff000090879858 != 0) && (*(code **)(DAT_ffff000090879858 + 0x20) != (code *)0x0)) {
    (**(code **)(DAT_ffff000090879858 + 0x20))(0x2b,auStack_9e8,0);
  }
  if ((DAT_ffff000090879860 != 0) && (*(code **)(DAT_ffff000090879860 + 0x20) != (code *)0x0)) {
    (**(code **)(DAT_ffff000090879860 + 0x20))(0x2c,auStack_9e8,0);
  }
  if ((DAT_ffff00009087a99c & 1) == 0) {
    FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
    DAT_ffff00009087a99c = 1;
  }
  *(uint *)(DAT_ffff00009087bf70 + 0x1c) = param_1 & 1;
  uVar1 = _DAT_ffff000032490034 & 3;
  if ((_DAT_ffff000032490034 & 3) == 0) {
    FUN_ffff00009074c368(0x2b);
  }
  else {
    _DAT_ffff000032490000 = _DAT_ffff000032490000 & 0xfffffffe;
    FUN_ffff00009074c368(0x2b);
    FUN_ffff00009074c2f0(0x2b,0,0);
  }
  if ((DAT_ffff00009087a99c & 1) == 0) {
    FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
    DAT_ffff00009087a99c = 1;
  }
  if (DAT_ffff00009087bf70 != 0) {
    if (((*(long *)(DAT_ffff00009087bf70 + 8) != 0) &&
        (pcVar7 = *(code **)(*(long *)(DAT_ffff00009087bf70 + 8) + 0x130), pcVar7 != (code *)0x0))
       && ((*pcVar7)(), (DAT_ffff00009087a99c & 1) == 0)) {
      FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
      DAT_ffff00009087a99c = 1;
    }
    if (DAT_ffff00009087bf70 != 0) {
      if (((*(long *)(DAT_ffff00009087bf70 + 8) != 0) &&
          (pcVar7 = *(code **)(*(long *)(DAT_ffff00009087bf70 + 8) + 0x138), pcVar7 != (code *)0x0))
         && ((*pcVar7)(), (DAT_ffff00009087a99c & 1) == 0)) {
        FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
        DAT_ffff00009087a99c = 1;
      }
      if (((DAT_ffff00009087bf70 != 0) && (*(long *)(DAT_ffff00009087bf70 + 8) != 0)) &&
         (pcVar7 = *(code **)(*(long *)(DAT_ffff00009087bf70 + 8) + 0x140), pcVar7 != (code *)0x0))
      {
        (*pcVar7)();
      }
    }
  }
  if (uVar1 != 0) {
    FUN_ffff00009074c2f0(0x2b,0,uVar1);
    _DAT_ffff000032490000 = _DAT_ffff000032490000 & 0xfffffffe | 1;
  }
  if ((DAT_ffff00009087a99c & 1) == 0) {
    FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
    DAT_ffff00009087a99c = 1;
  }
  iVar4 = FUN_ffff00009075c250(DAT_ffff00009087bf70);
  if (iVar4 != 0) {
    uVar3 = 0x2b0010;
    if (piVar6[0x1ae] != 1) {
      uVar3 = 0xa0004;
    }
    if ((DAT_ffff00009087a99c & 1) == 0) {
      FUN_ffff00009077ee50(0xa0004,&DAT_ffff00009087bf60,0,0x38);
      DAT_ffff00009087a99c = 1;
    }
    FUN_ffff000090756960(DAT_ffff00009087bf88,7,uVar3);
  }
  if ((DAT_ffff00009087a99c & 1) == 0) {
    FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
    DAT_ffff00009087a99c = 1;
  }
  FUN_ffff000090756900(DAT_ffff00009087bf88,7);
  if ((DAT_ffff00009087a99c & 1) == 0) {
    FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
    DAT_ffff00009087a99c = 1;
  }
  FUN_ffff000090756900(DAT_ffff00009087bf88,1);
  if ((DAT_ffff00009087a99c & 1) == 0) {
    FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
    DAT_ffff00009087a99c = 1;
  }
  DAT_ffff00009087bf50 = 1;
  DAT_ffff00009087bf60 = 1;
  return 0;
}


