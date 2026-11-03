// ffff00009073b8b0  size=632  FUN_ffff00009073b8b0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

ulong FUN_ffff00009073b8b0(long param_1,int param_2)

{
  uint uVar1;
  uint uVar2;
  ulong uVar3;
  uint uVar4;
  uint uVar5;
  uint uVar6;
  int iVar7;
  ulong uVar8;
  ulong uVar9;
  ulong uVar10;
  ulong uVar11;
  ulong uVar12;
  
  if (_DAT_ffff00009086e090 != 0 || _DAT_ffff00009086e098 != 0) {
    if (_DAT_ffff00009086e080 != 0) {
      uVar8 = FUN_ffff00009077f434(param_1,param_2,s_DFD_triggered_ffff0000907f2bb4);
      uVar6 = _DAT_ffff00009086e02c;
      uVar5 = _DAT_ffff00009086e028;
      uVar2 = _DAT_ffff00009086e024;
      uVar8 = uVar8 & 0xffffffff;
      uVar3 = (ulong)_DAT_ffff00009086e024;
      uVar11 = (ulong)_DAT_ffff00009086e02c;
      uVar4 = _DAT_ffff00009086e020 - _DAT_ffff00009086e024;
      if (_DAT_ffff00009086e090 != 0) {
        if (_DAT_ffff00009086e020 != _DAT_ffff00009086e024) {
          uVar10 = 0;
          uVar12 = (ulong)_DAT_ffff00009086e028;
          do {
            iVar7 = FUN_ffff00009077f434
                              (param_1 + uVar8,param_2 - (int)uVar8,s__CORE__d__ffff0000907dc7d8,
                               uVar10 & 0xffffffff);
            uVar1 = iVar7 + (int)uVar8;
            iVar7 = FUN_ffff00009077f434
                              (param_1 + (ulong)uVar1,param_2 - uVar1,
                               s_Top_pointer___0x_llx_ffff0000907e2db2,
                               *(undefined8 *)(_DAT_ffff00009086e090 + uVar10 * 0x10));
            uVar8 = (ulong)(iVar7 + uVar1);
            if (uVar5 != 0) {
              uVar9 = 0;
              do {
                iVar7 = FUN_ffff00009077f434
                                  (param_1 + uVar8,param_2 - (int)uVar8,
                                   s_return_stack_d____<0x_016llx>__ffff0000907ff063,
                                   uVar9 & 0xffffffff,
                                   *(undefined8 *)
                                    (*(long *)(_DAT_ffff00009086e090 + uVar10 * 0x10 + 8) +
                                    uVar9 * 8));
                uVar9 = uVar9 + 1;
                uVar8 = (ulong)(uint)(iVar7 + (int)uVar8);
              } while (uVar12 != uVar9);
            }
            iVar7 = FUN_ffff00009077f434(param_1 + uVar8,param_2 - (int)uVar8,&DAT_ffff0000907f81ce)
            ;
            uVar10 = uVar10 + 1;
            uVar8 = (ulong)(uint)(iVar7 + (int)uVar8);
          } while (uVar10 != uVar4);
        }
        thunk_FUN_ffff0000907c2ad8(_DAT_ffff00009086e090);
      }
      if (_DAT_ffff00009086e098 == 0) {
        return uVar8;
      }
      if (uVar2 != 0) {
        uVar10 = 0;
        do {
          iVar7 = FUN_ffff00009077f434
                            (param_1 + uVar8,param_2 - (int)uVar8,s__CORE__d__ffff0000907dc7d8,
                             uVar4 + (int)uVar10);
          uVar2 = iVar7 + (int)uVar8;
          iVar7 = FUN_ffff00009077f434
                            (param_1 + (ulong)uVar2,param_2 - uVar2,
                             s_Top_pointer___0x_llx_ffff0000907e2db2,
                             *(undefined8 *)(_DAT_ffff00009086e098 + uVar10 * 0x10));
          uVar8 = (ulong)(iVar7 + uVar2);
          if (uVar6 != 0) {
            uVar12 = 0;
            do {
              iVar7 = FUN_ffff00009077f434
                                (param_1 + uVar8,param_2 - (int)uVar8,
                                 s_return_stack_d____<0x_016llx>__ffff0000907ff063,
                                 uVar12 & 0xffffffff,
                                 *(undefined8 *)
                                  (*(long *)(_DAT_ffff00009086e098 + uVar10 * 0x10 + 8) + uVar12 * 8
                                  ));
              uVar12 = uVar12 + 1;
              uVar8 = (ulong)(uint)(iVar7 + (int)uVar8);
            } while (uVar11 != uVar12);
          }
          iVar7 = FUN_ffff00009077f434(param_1 + uVar8,param_2 - (int)uVar8,&DAT_ffff0000907f81ce);
          uVar10 = uVar10 + 1;
          uVar8 = (ulong)(uint)(iVar7 + (int)uVar8);
        } while (uVar10 != uVar3);
      }
      thunk_FUN_ffff0000907c2ad8(_DAT_ffff00009086e098);
      return uVar8;
    }
    if (_DAT_ffff00009086e090 != 0) {
      thunk_FUN_ffff0000907c2ad8(_DAT_ffff00009086e090);
    }
    if (_DAT_ffff00009086e098 != 0) {
      thunk_FUN_ffff0000907c2ad8(_DAT_ffff00009086e098);
    }
  }
  return 0;
}


