
int FUN_ffff0000907c65d0(long param_1,long param_2,ulong param_3)

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
    FUN_ffff0000907c5198(param_1,param_2,&local_58);
    if (local_58 == 0) {
      lVar3 = 0;
      iVar1 = 0x10800b;
    }
    else {
      DAT_ffff00009090e610 = param_3;
      if (param_3 == 0xffffffffffffffff) {
        FUN_ffff00009077e11c(s__AVB__Persist_offset_is_half_of_p_ffff00009080369c,param_2);
        DAT_ffff00009090e610 = (local_58 >> 1) + 0xf & 0xfffffffffffffff0;
      }
      if (((DAT_ffff00009090e610 < 0xffffffffffffffa0) && (DAT_ffff00009090e610 < local_58)) &&
         (DAT_ffff00009090e610 + 0x60 <= local_58)) {
        iVar1 = FUN_ffff0000907c4ef0(param_2,DAT_ffff00009090e610,&local_68);
        if (iVar1 == 0) {
          uVar2 = local_58 - local_68;
          if (0x1ff < uVar2) {
            uVar2 = 0x200;
          }
          DAT_ffff00009090e61c = (undefined4)uVar2;
          lVar3 = FUN_ffff0000907c1544();
          iVar1 = 0x100006;
          if (lVar3 != 0) {
            iVar1 = (**(code **)(param_1 + 0x18))
                              (param_1,param_2,local_68,DAT_ffff00009090e61c,lVar3,&uStack_60);
            if (iVar1 == 0) {
              iVar1 = FUN_ffff0000907c6874(lVar3,&local_6c);
              if (iVar1 == 0) {
                DAT_ffff00009090e600 = FUN_ffff0000907c1544(0x100);
                if (DAT_ffff00009090e600 == 0) {
                  FUN_ffff00009077e11c(s_g_persist_des_NULL_ffff0000907e2406);
                  iVar1 = 0x100006;
                }
                else {
                  thunk_FUN_ffff00009077ee50(DAT_ffff00009090e600,0,0x100);
                  if (local_6c == 0) {
                    iVar1 = FUN_ffff0000907c6914(param_1,param_2,local_68,lVar3);
                    if (iVar1 != 0) goto LAB_ffff0000907c66e4;
                  }
                  else {
                    iVar1 = FUN_ffff0000907c69bc(lVar3,DAT_ffff00009090e61c);
                    if (iVar1 != 0) {
                      FUN_ffff00009077e11c
                                (s_Parse_persist_value_fail__ret___0_ffff000090801c7d,iVar1);
                      thunk_FUN_ffff00009077ee50(DAT_ffff00009090e600,0,0x100);
                    }
                  }
                  DAT_ffff00009090e60c = 1;
                  iVar1 = 0;
                }
              }
            }
            else {
              FUN_ffff00009077e11c(s_Error_loading_persist_value_from_ffff0000908001f8,param_2);
            }
          }
        }
        else {
          lVar3 = 0;
        }
      }
      else {
        FUN_ffff00009077e11c
                  (s__AVB__persist_offset___0x_llx_is_ffff0000907e8b04,DAT_ffff00009090e610);
        lVar3 = 0;
        iVar1 = 0x10800c;
      }
    }
  }
LAB_ffff0000907c66e4:
  FUN_ffff00009077e11c(s__AVB____s__partition_size___0x_l_ffff0000907ebd2d,param_2,local_58);
  FUN_ffff00009077e11c(s__AVB__persist_offset___0x_llx_ffff0000907d8adb,DAT_ffff00009090e610);
  FUN_ffff00009077e11c(s__AVB__persist_allocate_size___0x_ffff0000907dd802,DAT_ffff00009090e61c);
  FUN_ffff00009077e11c(s__AVB__persist_area_init_end___d__ffff0000907e3f97,DAT_ffff00009090e60c);
  FUN_ffff00009077e11c(s__AVB__persist_ret___0x_x__ffff0000907dd826,iVar1);
  if (lVar3 != 0) {
    FUN_ffff0000907c1014(lVar3);
  }
  return iVar1;
}


