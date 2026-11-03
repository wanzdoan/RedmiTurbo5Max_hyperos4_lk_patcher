
int FUN_ffff0000be09a7c8(long param_1,long param_2,ulong param_3)

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
    FUN_ffff0000be0998f4(param_1,param_2,&local_58);
    if (local_58 == 0) {
      lVar3 = 0;
      iVar1 = 0x10800b;
    }
    else {
      DAT_ffff0000be1b7e58 = param_3;
      if (param_3 == 0xffffffffffffffff) {
        FUN_ffff0000be05fd98(s__AVB__Persist_offset_is_half_of_p_ffff0000be0c6e1d,param_2);
        DAT_ffff0000be1b7e58 = (local_58 >> 1) + 0xf & 0xfffffffffffffff0;
      }
      if (((DAT_ffff0000be1b7e58 < 0xffffffffffffffa0) && (DAT_ffff0000be1b7e58 < local_58)) &&
         (DAT_ffff0000be1b7e58 + 0x60 <= local_58)) {
        iVar1 = FUN_ffff0000be099878(param_2,DAT_ffff0000be1b7e58,&local_68);
        if (iVar1 == 0) {
          uVar2 = local_58 - local_68;
          if (0x1ff < uVar2) {
            uVar2 = 0x200;
          }
          DAT_ffff0000be1b7e64 = (undefined4)uVar2;
          lVar3 = FUN_ffff0000be0965b4();
          iVar1 = 0x100006;
          if (lVar3 != 0) {
            iVar1 = (**(code **)(param_1 + 0x18))
                              (param_1,param_2,local_68,DAT_ffff0000be1b7e64,lVar3,&uStack_60);
            if (iVar1 == 0) {
              iVar1 = FUN_ffff0000be09aa6c(lVar3,&local_6c);
              if (iVar1 == 0) {
                DAT_ffff0000be1b7e48 = FUN_ffff0000be0965b4(0x100);
                if (DAT_ffff0000be1b7e48 == 0) {
                  FUN_ffff0000be05fd98(s_g_persist_des_NULL_ffff0000be0b0763);
                  iVar1 = 0x100006;
                }
                else {
                  thunk_FUN_ffff0000be060ae0(DAT_ffff0000be1b7e48,0,0x100);
                  if (local_6c == 0) {
                    iVar1 = FUN_ffff0000be09ab0c(param_1,param_2,local_68,lVar3);
                    if (iVar1 != 0) goto LAB_ffff0000be09a8dc;
                  }
                  else {
                    iVar1 = FUN_ffff0000be09abb4(lVar3,DAT_ffff0000be1b7e64);
                    if (iVar1 != 0) {
                      FUN_ffff0000be05fd98
                                (s_Parse_persist_value_fail__ret___0_ffff0000be0c5cc6,iVar1);
                      thunk_FUN_ffff0000be060ae0(DAT_ffff0000be1b7e48,0,0x100);
                    }
                  }
                  DAT_ffff0000be1b7e54 = 1;
                  iVar1 = 0;
                }
              }
            }
            else {
              FUN_ffff0000be05fd98(s_Error_loading_persist_value_from_ffff0000be0c4b52,param_2);
            }
          }
        }
        else {
          lVar3 = 0;
        }
      }
      else {
        FUN_ffff0000be05fd98
                  (s__AVB__persist_offset___0x_llx_is_ffff0000be0b4d86,DAT_ffff0000be1b7e58);
        lVar3 = 0;
        iVar1 = 0x10800c;
      }
    }
  }
LAB_ffff0000be09a8dc:
  FUN_ffff0000be05fd98(s__AVB____s__partition_size___0x_l_ffff0000be0b71f1,param_2,local_58);
  FUN_ffff0000be05fd98(s__AVB__persist_offset___0x_llx_ffff0000be0a9da2,DAT_ffff0000be1b7e58);
  FUN_ffff0000be05fd98(s__AVB__persist_allocate_size___0x_ffff0000be0ad198,DAT_ffff0000be1b7e64);
  FUN_ffff0000be05fd98(s__AVB__persist_area_init_end___d__ffff0000be0b185e,DAT_ffff0000be1b7e54);
  FUN_ffff0000be05fd98(s__AVB__persist_ret___0x_x__ffff0000be0ad1bc,iVar1);
  if (lVar3 != 0) {
    FUN_ffff0000be096588(lVar3);
  }
  return iVar1;
}


