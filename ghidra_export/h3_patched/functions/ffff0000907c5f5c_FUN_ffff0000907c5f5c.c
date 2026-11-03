// ffff0000907c5f5c  size=676  FUN_ffff0000907c5f5c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000907c5f5c(long param_1,long param_2,ulong param_3)

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
    FUN_ffff0000907c4b24(param_1,param_2,&local_58);
    if (local_58 == 0) {
      lVar3 = 0;
      iVar1 = 0x10800b;
    }
    else {
      _DAT_ffff00009090e550 = param_3;
      if (param_3 == 0xffffffffffffffff) {
        FUN_ffff00009077de54(s__AVB__Persist_offset_is_half_of_p_ffff000090803474,param_2);
        _DAT_ffff00009090e550 = (local_58 >> 1) + 0xf & 0xfffffffffffffff0;
      }
      if (((_DAT_ffff00009090e550 < 0xffffffffffffffa0) && (_DAT_ffff00009090e550 < local_58)) &&
         (_DAT_ffff00009090e550 + 0x60 <= local_58)) {
        iVar1 = FUN_ffff0000907c487c(param_2,_DAT_ffff00009090e550,&local_68);
        if (iVar1 == 0) {
          uVar2 = local_58 - local_68;
          if (0x1ff < uVar2) {
            uVar2 = 0x200;
          }
          _DAT_ffff00009090e55c = (undefined4)uVar2;
          lVar3 = FUN_ffff0000907c0ed0();
          iVar1 = 0x100006;
          if (lVar3 != 0) {
            iVar1 = (**(code **)(param_1 + 0x18))
                              (param_1,param_2,local_68,_DAT_ffff00009090e55c,lVar3,&uStack_60);
            if (iVar1 == 0) {
              iVar1 = FUN_ffff0000907c6200(lVar3,&local_6c);
              if (iVar1 == 0) {
                _DAT_ffff00009090e540 = FUN_ffff0000907c0ed0(0x100);
                if (_DAT_ffff00009090e540 == 0) {
                  FUN_ffff00009077de54(s_g_persist_des_NULL_ffff0000907e231e);
                  iVar1 = 0x100006;
                }
                else {
                  thunk_FUN_ffff00009077eb88(_DAT_ffff00009090e540,0,0x100);
                  if (local_6c == 0) {
                    iVar1 = FUN_ffff0000907c62a0(param_1,param_2,local_68,lVar3);
                    if (iVar1 != 0) goto LAB_ffff0000907c6070;
                  }
                  else {
                    iVar1 = FUN_ffff0000907c6348(lVar3,_DAT_ffff00009090e55c);
                    if (iVar1 != 0) {
                      FUN_ffff00009077de54
                                (s_Parse_persist_value_fail__ret___0_ffff000090801aa1,iVar1);
                      thunk_FUN_ffff00009077eb88(_DAT_ffff00009090e540,0,0x100);
                    }
                  }
                  _DAT_ffff00009090e54c = 1;
                  iVar1 = 0;
                }
              }
            }
            else {
              FUN_ffff00009077de54(s_Error_loading_persist_value_from_ffff000090800050,param_2);
            }
          }
        }
        else {
          lVar3 = 0;
        }
      }
      else {
        FUN_ffff00009077de54
                  (s__AVB__persist_offset___0x_llx_is_ffff0000907e89a9,_DAT_ffff00009090e550);
        lVar3 = 0;
        iVar1 = 0x10800c;
      }
    }
  }
LAB_ffff0000907c6070:
  FUN_ffff00009077de54(s__AVB____s__partition_size___0x_l_ffff0000907ebb93,param_2,local_58);
  FUN_ffff00009077de54(s__AVB__persist_offset___0x_llx_ffff0000907d8a5a,_DAT_ffff00009090e550);
  FUN_ffff00009077de54(s__AVB__persist_allocate_size___0x_ffff0000907dd748,_DAT_ffff00009090e55c);
  FUN_ffff00009077de54(s__AVB__persist_area_init_end___d__ffff0000907e3eaf,_DAT_ffff00009090e54c);
  FUN_ffff00009077de54(s__AVB__persist_ret___0x_x__ffff0000907dd76c,iVar1);
  if (lVar3 != 0) {
    FUN_ffff0000907c09a0(lVar3);
  }
  return iVar1;
}


