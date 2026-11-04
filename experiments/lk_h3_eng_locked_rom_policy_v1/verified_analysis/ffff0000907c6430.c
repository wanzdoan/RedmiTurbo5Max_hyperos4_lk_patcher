
int FUN_ffff0000907c6430(long param_1,long param_2,ulong param_3)

{
  int iVar1;
  ulong uVar2;
  long lVar3;
  int local_6c;
  long local_68;
  undefined8 uStack_60;
  ulong local_58;

  local_58 = 0;
  local_68 = 0;
  uStack_60 = 0;
  local_6c = 0;
  if (param_2 == 0) {
    lVar3 = 0;
    iVar1 = 0x100006;
  }
  else {
    FUN_ffff0000907c50a8(param_1,param_2,&local_58);
    if (local_58 == 0) {
      lVar3 = 0;
      iVar1 = 0x10800b;
    }
    else {
      DAT_ffff00009090e550 = param_3;
      if (param_3 == 0xffffffffffffffff) {
        FUN_ffff00009077ea84(s__AVB__Persist_offset_is_half_of_p_ffff0000907fc67d,param_2);
        DAT_ffff00009090e550 = (local_58 >> 1) + 0xf & 0xfffffffffffffff0;
      }
      if (((DAT_ffff00009090e550 < 0xffffffffffffffa0) && (DAT_ffff00009090e550 < local_58)) &&
         (DAT_ffff00009090e550 + 0x60 <= local_58)) {
        iVar1 = FUN_ffff0000907c5018(param_2,DAT_ffff00009090e550,&local_68);
        if (iVar1 == 0) {
          uVar2 = local_58 - local_68;
          if (0x1ff < uVar2) {
            uVar2 = 0x200;
          }
          DAT_ffff00009090e55c = (undefined4)uVar2;
          lVar3 = FUN_ffff0000907c1504();
          iVar1 = 0x100006;
          if (lVar3 != 0) {
            iVar1 = (**(code **)(param_1 + 0x18))
                              (param_1,param_2,local_68,DAT_ffff00009090e55c,lVar3,&uStack_60);
            if (iVar1 == 0) {
              iVar1 = FUN_ffff0000907c66d4(lVar3,&local_6c);
              if (iVar1 == 0) {
                DAT_ffff00009090e540 = FUN_ffff0000907c1504(0x100);
                if (DAT_ffff00009090e540 == 0) {
                  FUN_ffff00009077ea84(s_g_persist_des_NULL_ffff0000907dab2f);
                  iVar1 = 0x100006;
                }
                else {
                  thunk_FUN_ffff00009077f7b8(DAT_ffff00009090e540,0,0x100);
                  if (local_6c == 0) {
                    iVar1 = FUN_ffff0000907c6774(param_1,param_2,local_68,lVar3);
                    if (iVar1 != 0) goto LAB_ffff0000907c6544;
                  }
                  else {
                    iVar1 = FUN_ffff0000907c681c(lVar3,DAT_ffff00009090e55c);
                    if (iVar1 != 0) {
                      FUN_ffff00009077ea84
                                (s_Parse_persist_value_fail__ret___0_ffff0000907fac00,iVar1);
                      thunk_FUN_ffff00009077f7b8(DAT_ffff00009090e540,0,0x100);
                    }
                  }
                  DAT_ffff00009090e54c = 1;
                  iVar1 = 0;
                }
              }
            }
            else {
              FUN_ffff00009077ea84(s_Error_loading_persist_value_from_ffff0000907f90dd,param_2);
            }
          }
        }
        else {
          lVar3 = 0;
        }
      }
      else {
        FUN_ffff00009077ea84
                  (s__AVB__persist_offset___0x_llx_is_ffff0000907e13e0,DAT_ffff00009090e550);
        lVar3 = 0;
        iVar1 = 0x10800c;
      }
    }
  }
LAB_ffff0000907c6544:
  FUN_ffff00009077ea84(s__AVB____s__partition_size___0x_l_ffff0000907e4868,param_2,local_58);
  FUN_ffff00009077ea84(s__AVB__persist_offset___0x_llx_ffff0000907d0ecf,DAT_ffff00009090e550);
  FUN_ffff00009077ea84(s__AVB__persist_allocate_size___0x_ffff0000907d5c04,DAT_ffff00009090e55c);
  FUN_ffff00009077ea84(s__AVB__persist_area_init_end___d__ffff0000907dc67b,DAT_ffff00009090e54c);
  FUN_ffff00009077ea84(s__AVB__persist_ret___0x_x__ffff0000907d5c28,iVar1);
  if (lVar3 != 0) {
    FUN_ffff0000907c0fd4(lVar3);
  }
  return iVar1;
}
