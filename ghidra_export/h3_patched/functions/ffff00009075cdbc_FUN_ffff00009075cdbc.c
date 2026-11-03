// ffff00009075cdbc  size=1340  FUN_ffff00009075cdbc

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009075cdbc(uint param_1,undefined8 param_2,long param_3)

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
  
  FUN_ffff000090756920();
  if ((DAT_ffff00009087a904 & 1) == 0) {
    FUN_ffff00009077eb88(&DAT_ffff00009087bec8,0,0x38);
    DAT_ffff00009087a904 = 1;
  }
  if (_DAT_ffff00009087bed8 == 0) {
    if (param_3 != 0) {
      FUN_ffff00009077de54
                (s__s__probe_of_lcm__s_ffff0000907f88e9,s_primary_display_sw_init_ffff0000907f3259,
                 param_3);
    }
    lVar5 = FUN_ffff00009075b9a8(param_3,param_2);
    if ((DAT_ffff00009087a904 & 1) == 0) {
      FUN_ffff00009077eb88(&DAT_ffff00009087bec8,0,0x38);
      DAT_ffff00009087a904 = 1;
    }
    _DAT_ffff00009087bed8 = lVar5;
    if (lVar5 == 0) {
      return 3;
    }
  }
  piVar6 = (int *)FUN_ffff00009075bdf0(_DAT_ffff00009087bed8);
  if (piVar6 == (int *)0x0) {
    return 3;
  }
  FUN_ffff00009077eb88(auStack_9e8,0,0x998);
  if (piVar6[0x98] == 1) {
    FUN_ffff00009075a5ec(piVar6);
  }
  if (_DAT_ffff00009087beb4 == 0) {
    FUN_ffff00009075cc78(param_2);
  }
  if ((DAT_ffff00009087a904 & 1) == 0) {
    FUN_ffff00009077eb88(&DAT_ffff00009087bec8,0,0x38);
    DAT_ffff00009087a904 = 1;
  }
  uVar2 = _DAT_ffff00009087bef0;
  uVar3 = FUN_ffff00009075bfb8(_DAT_ffff00009087bed8);
  FUN_ffff000090754d4c(uVar2,uVar3);
  if ((DAT_ffff00009087a904 & 1) == 0) {
    FUN_ffff00009077eb88(&DAT_ffff00009087bec8,0,0x38);
    DAT_ffff00009087a904 = 1;
  }
  FUN_ffff000090755484(_DAT_ffff00009087bef0,0,param_2,0);
  FUN_ffff00009077eb88(auStack_9e8,0,0x998);
  FUN_ffff00009077e858(auStack_708,piVar6 + 0x5c,0x5b0);
  if ((DAT_ffff00009087a904 & 1) == 0) {
    FUN_ffff00009077eb88(&DAT_ffff00009087bec8,0,0x38);
    DAT_ffff00009087a904 = 1;
  }
  local_c0 = FUN_ffff00009075be50(_DAT_ffff00009087bed8);
  if ((DAT_ffff00009087a904 & 1) == 0) {
    FUN_ffff00009077eb88(&DAT_ffff00009087bec8,0,0x38);
    DAT_ffff00009087a904 = 1;
  }
  local_bc = FUN_ffff00009075be90(_DAT_ffff00009087bed8);
  if (*piVar6 == 1) {
    uVar1 = piVar6[0x4e];
  }
  else {
    if (*piVar6 != 2) goto LAB_ffff00009075d010;
    uVar1 = piVar6[0x65];
  }
  if (uVar1 < 3) {
    local_c4 = *(undefined4 *)(&DAT_ffff00009080e270 + (long)(int)uVar1 * 4);
  }
LAB_ffff00009075d010:
  if ((_DAT_ffff0000908797c0 != 0) && (*(code **)(_DAT_ffff0000908797c0 + 0x20) != (code *)0x0)) {
    (**(code **)(_DAT_ffff0000908797c0 + 0x20))(0x2b,auStack_9e8,0);
  }
  if ((_DAT_ffff0000908797c8 != 0) && (*(code **)(_DAT_ffff0000908797c8 + 0x20) != (code *)0x0)) {
    (**(code **)(_DAT_ffff0000908797c8 + 0x20))(0x2c,auStack_9e8,0);
  }
  if ((DAT_ffff00009087a904 & 1) == 0) {
    FUN_ffff00009077eb88(&DAT_ffff00009087bec8,0,0x38);
    DAT_ffff00009087a904 = 1;
  }
  *(uint *)(_DAT_ffff00009087bed8 + 0x1c) = param_1 & 1;
  uVar1 = _DAT_ffff000032490034 & 3;
  if ((_DAT_ffff000032490034 & 3) == 0) {
    FUN_ffff00009074c0d0(0x2b);
  }
  else {
    _DAT_ffff000032490000 = _DAT_ffff000032490000 & 0xfffffffe;
    FUN_ffff00009074c0d0(0x2b);
    FUN_ffff00009074c058(0x2b,0,0);
  }
  if ((DAT_ffff00009087a904 & 1) == 0) {
    FUN_ffff00009077eb88(&DAT_ffff00009087bec8,0,0x38);
    DAT_ffff00009087a904 = 1;
  }
  if (_DAT_ffff00009087bed8 != 0) {
    if (((*(long *)(_DAT_ffff00009087bed8 + 8) != 0) &&
        (pcVar7 = *(code **)(*(long *)(_DAT_ffff00009087bed8 + 8) + 0x130), pcVar7 != (code *)0x0))
       && ((*pcVar7)(), (DAT_ffff00009087a904 & 1) == 0)) {
      FUN_ffff00009077eb88(&DAT_ffff00009087bec8,0,0x38);
      DAT_ffff00009087a904 = 1;
    }
    if (_DAT_ffff00009087bed8 != 0) {
      if (((*(long *)(_DAT_ffff00009087bed8 + 8) != 0) &&
          (pcVar7 = *(code **)(*(long *)(_DAT_ffff00009087bed8 + 8) + 0x138), pcVar7 != (code *)0x0)
          ) && ((*pcVar7)(), (DAT_ffff00009087a904 & 1) == 0)) {
        FUN_ffff00009077eb88(&DAT_ffff00009087bec8,0,0x38);
        DAT_ffff00009087a904 = 1;
      }
      if (((_DAT_ffff00009087bed8 != 0) && (*(long *)(_DAT_ffff00009087bed8 + 8) != 0)) &&
         (pcVar7 = *(code **)(*(long *)(_DAT_ffff00009087bed8 + 8) + 0x140), pcVar7 != (code *)0x0))
      {
        (*pcVar7)();
      }
    }
  }
  if (uVar1 != 0) {
    FUN_ffff00009074c058(0x2b,0,uVar1);
    _DAT_ffff000032490000 = _DAT_ffff000032490000 & 0xfffffffe | 1;
  }
  if ((DAT_ffff00009087a904 & 1) == 0) {
    FUN_ffff00009077eb88(&DAT_ffff00009087bec8,0,0x38);
    DAT_ffff00009087a904 = 1;
  }
  iVar4 = FUN_ffff00009075bfb8(_DAT_ffff00009087bed8);
  if (iVar4 != 0) {
    uVar3 = 0x2b0010;
    if (piVar6[0x1ae] != 1) {
      uVar3 = 0xa0004;
    }
    if ((DAT_ffff00009087a904 & 1) == 0) {
      FUN_ffff00009077eb88(0xa0004,&DAT_ffff00009087bec8,0,0x38);
      DAT_ffff00009087a904 = 1;
    }
    FUN_ffff0000907566c8(_DAT_ffff00009087bef0,7,uVar3);
  }
  if ((DAT_ffff00009087a904 & 1) == 0) {
    FUN_ffff00009077eb88(&DAT_ffff00009087bec8,0,0x38);
    DAT_ffff00009087a904 = 1;
  }
  FUN_ffff000090756668(_DAT_ffff00009087bef0,7);
  if ((DAT_ffff00009087a904 & 1) == 0) {
    FUN_ffff00009077eb88(&DAT_ffff00009087bec8,0,0x38);
    DAT_ffff00009087a904 = 1;
  }
  FUN_ffff000090756668(_DAT_ffff00009087bef0,1);
  if ((DAT_ffff00009087a904 & 1) == 0) {
    FUN_ffff00009077eb88(&DAT_ffff00009087bec8,0,0x38);
    DAT_ffff00009087a904 = 1;
  }
  DAT_ffff00009087beb8 = 1;
  _DAT_ffff00009087bec8 = 1;
  return 0;
}


