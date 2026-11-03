
ulong FUN_ffff0000b8022578(long param_1,int param_2)

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
  
  if (DAT_ffff0000b811bbb8 != 0 || DAT_ffff0000b811bbc0 != 0) {
    if (DAT_ffff0000b811bba8 != 0) {
      uVar8 = FUN_ffff0000b804d74c(param_1,param_2,s_DFD_triggered_ffff0000b8098c56);
      uVar6 = DAT_ffff0000b811bb54;
      uVar5 = DAT_ffff0000b811bb50;
      uVar2 = DAT_ffff0000b811bb4c;
      uVar8 = uVar8 & 0xffffffff;
      uVar3 = (ulong)DAT_ffff0000b811bb4c;
      uVar11 = (ulong)DAT_ffff0000b811bb54;
      uVar4 = DAT_ffff0000b811bb48 - DAT_ffff0000b811bb4c;
      if (DAT_ffff0000b811bbb8 != 0) {
        if (DAT_ffff0000b811bb48 != DAT_ffff0000b811bb4c) {
          uVar10 = 0;
          uVar12 = (ulong)DAT_ffff0000b811bb50;
          do {
            iVar7 = FUN_ffff0000b804d74c
                              (param_1 + uVar8,param_2 - (int)uVar8,s__CORE__d__ffff0000b808c6f0,
                               uVar10 & 0xffffffff);
            uVar1 = iVar7 + (int)uVar8;
            iVar7 = FUN_ffff0000b804d74c
                              (param_1 + (ulong)uVar1,param_2 - uVar1,
                               s_Top_pointer___0x_llx_ffff0000b808fbe2,
                               *(undefined8 *)(DAT_ffff0000b811bbb8 + uVar10 * 0x10));
            uVar8 = (ulong)(iVar7 + uVar1);
            if (uVar5 != 0) {
              uVar9 = 0;
              do {
                iVar7 = FUN_ffff0000b804d74c
                                  (param_1 + uVar8,param_2 - (int)uVar8,
                                   s_return_stack_d____<0x_016llx>__ffff0000b809f709,
                                   uVar9 & 0xffffffff,
                                   *(undefined8 *)
                                    (*(long *)(DAT_ffff0000b811bbb8 + uVar10 * 0x10 + 8) + uVar9 * 8
                                    ));
                uVar9 = uVar9 + 1;
                uVar8 = (ulong)(uint)(iVar7 + (int)uVar8);
              } while (uVar12 != uVar9);
            }
            iVar7 = FUN_ffff0000b804d74c(param_1 + uVar8,param_2 - (int)uVar8,&DAT_ffff0000b809b862)
            ;
            uVar10 = uVar10 + 1;
            uVar8 = (ulong)(uint)(iVar7 + (int)uVar8);
          } while (uVar10 != uVar4);
        }
        thunk_FUN_ffff0000b8082558(DAT_ffff0000b811bbb8);
      }
      if (DAT_ffff0000b811bbc0 == 0) {
        return uVar8;
      }
      if (uVar2 != 0) {
        uVar10 = 0;
        do {
          iVar7 = FUN_ffff0000b804d74c
                            (param_1 + uVar8,param_2 - (int)uVar8,s__CORE__d__ffff0000b808c6f0,
                             uVar4 + (int)uVar10);
          uVar2 = iVar7 + (int)uVar8;
          iVar7 = FUN_ffff0000b804d74c
                            (param_1 + (ulong)uVar2,param_2 - uVar2,
                             s_Top_pointer___0x_llx_ffff0000b808fbe2,
                             *(undefined8 *)(DAT_ffff0000b811bbc0 + uVar10 * 0x10));
          uVar8 = (ulong)(iVar7 + uVar2);
          if (uVar6 != 0) {
            uVar12 = 0;
            do {
              iVar7 = FUN_ffff0000b804d74c
                                (param_1 + uVar8,param_2 - (int)uVar8,
                                 s_return_stack_d____<0x_016llx>__ffff0000b809f709,
                                 uVar12 & 0xffffffff,
                                 *(undefined8 *)
                                  (*(long *)(DAT_ffff0000b811bbc0 + uVar10 * 0x10 + 8) + uVar12 * 8)
                                );
              uVar12 = uVar12 + 1;
              uVar8 = (ulong)(uint)(iVar7 + (int)uVar8);
            } while (uVar11 != uVar12);
          }
          iVar7 = FUN_ffff0000b804d74c(param_1 + uVar8,param_2 - (int)uVar8,&DAT_ffff0000b809b862);
          uVar10 = uVar10 + 1;
          uVar8 = (ulong)(uint)(iVar7 + (int)uVar8);
        } while (uVar10 != uVar3);
      }
      thunk_FUN_ffff0000b8082558(DAT_ffff0000b811bbc0);
      return uVar8;
    }
    if (DAT_ffff0000b811bbb8 != 0) {
      thunk_FUN_ffff0000b8082558(DAT_ffff0000b811bbb8);
    }
    if (DAT_ffff0000b811bbc0 != 0) {
      thunk_FUN_ffff0000b8082558(DAT_ffff0000b811bbc0);
    }
  }
  return 0;
}


