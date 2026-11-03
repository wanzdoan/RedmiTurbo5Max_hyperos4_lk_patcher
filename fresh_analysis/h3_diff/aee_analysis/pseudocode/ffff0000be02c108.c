
ulong FUN_ffff0000be02c108(long param_1,int param_2)

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
  
  if (DAT_ffff0000be1183f0 != 0 || DAT_ffff0000be1183f8 != 0) {
    if (DAT_ffff0000be1183e0 != 0) {
      uVar8 = FUN_ffff0000be0611dc(param_1,param_2,s_DFD_triggered_ffff0000be0bbe63);
      uVar6 = DAT_ffff0000be11838c;
      uVar5 = DAT_ffff0000be118388;
      uVar2 = DAT_ffff0000be118384;
      uVar8 = uVar8 & 0xffffffff;
      uVar3 = (ulong)DAT_ffff0000be118384;
      uVar11 = (ulong)DAT_ffff0000be11838c;
      uVar4 = DAT_ffff0000be118380 - DAT_ffff0000be118384;
      if (DAT_ffff0000be1183f0 != 0) {
        if (DAT_ffff0000be118380 != DAT_ffff0000be118384) {
          uVar10 = 0;
          uVar12 = (ulong)DAT_ffff0000be118388;
          do {
            iVar7 = FUN_ffff0000be0611dc
                              (param_1 + uVar8,param_2 - (int)uVar8,s__CORE__d__ffff0000be0ac835,
                               uVar10 & 0xffffffff);
            uVar1 = iVar7 + (int)uVar8;
            iVar7 = FUN_ffff0000be0611dc
                              (param_1 + (ulong)uVar1,param_2 - uVar1,
                               s_Top_pointer___0x_llx_ffff0000be0b0c65,
                               *(undefined8 *)(DAT_ffff0000be1183f0 + uVar10 * 0x10));
            uVar8 = (ulong)(iVar7 + uVar1);
            if (uVar5 != 0) {
              uVar9 = 0;
              do {
                iVar7 = FUN_ffff0000be0611dc
                                  (param_1 + uVar8,param_2 - (int)uVar8,
                                   s_return_stack_d____<0x_016llx>__ffff0000be0c407e,
                                   uVar9 & 0xffffffff,
                                   *(undefined8 *)
                                    (*(long *)(DAT_ffff0000be1183f0 + uVar10 * 0x10 + 8) + uVar9 * 8
                                    ));
                uVar9 = uVar9 + 1;
                uVar8 = (ulong)(uint)(iVar7 + (int)uVar8);
              } while (uVar12 != uVar9);
            }
            iVar7 = FUN_ffff0000be0611dc(param_1 + uVar8,param_2 - (int)uVar8,&DAT_ffff0000be0bf441)
            ;
            uVar10 = uVar10 + 1;
            uVar8 = (ulong)(uint)(iVar7 + (int)uVar8);
          } while (uVar10 != uVar4);
        }
        thunk_FUN_ffff0000be09937c(DAT_ffff0000be1183f0);
      }
      if (DAT_ffff0000be1183f8 == 0) {
        return uVar8;
      }
      if (uVar2 != 0) {
        uVar10 = 0;
        do {
          iVar7 = FUN_ffff0000be0611dc
                            (param_1 + uVar8,param_2 - (int)uVar8,s__CORE__d__ffff0000be0ac835,
                             uVar4 + (int)uVar10);
          uVar2 = iVar7 + (int)uVar8;
          iVar7 = FUN_ffff0000be0611dc
                            (param_1 + (ulong)uVar2,param_2 - uVar2,
                             s_Top_pointer___0x_llx_ffff0000be0b0c65,
                             *(undefined8 *)(DAT_ffff0000be1183f8 + uVar10 * 0x10));
          uVar8 = (ulong)(iVar7 + uVar2);
          if (uVar6 != 0) {
            uVar12 = 0;
            do {
              iVar7 = FUN_ffff0000be0611dc
                                (param_1 + uVar8,param_2 - (int)uVar8,
                                 s_return_stack_d____<0x_016llx>__ffff0000be0c407e,
                                 uVar12 & 0xffffffff,
                                 *(undefined8 *)
                                  (*(long *)(DAT_ffff0000be1183f8 + uVar10 * 0x10 + 8) + uVar12 * 8)
                                );
              uVar12 = uVar12 + 1;
              uVar8 = (ulong)(uint)(iVar7 + (int)uVar8);
            } while (uVar11 != uVar12);
          }
          iVar7 = FUN_ffff0000be0611dc(param_1 + uVar8,param_2 - (int)uVar8,&DAT_ffff0000be0bf441);
          uVar10 = uVar10 + 1;
          uVar8 = (ulong)(uint)(iVar7 + (int)uVar8);
        } while (uVar10 != uVar3);
      }
      thunk_FUN_ffff0000be09937c(DAT_ffff0000be1183f8);
      return uVar8;
    }
    if (DAT_ffff0000be1183f0 != 0) {
      thunk_FUN_ffff0000be09937c(DAT_ffff0000be1183f0);
    }
    if (DAT_ffff0000be1183f8 != 0) {
      thunk_FUN_ffff0000be09937c(DAT_ffff0000be1183f8);
    }
  }
  return 0;
}


