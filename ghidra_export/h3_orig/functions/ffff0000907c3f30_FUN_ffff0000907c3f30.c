// ffff0000907c3f30  size=556  FUN_ffff0000907c3f30

void FUN_ffff0000907c3f30
               (undefined8 param_1,ulong param_2,ulong param_3,undefined8 param_4,
               undefined8 *param_5,int param_6)

{
  int iVar1;
  undefined8 uVar2;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  
  FUN_ffff00009077de54(s__s__entry_ffff0000907e70db,s_fill_rect_with_content_ffff0000907da2f6);
  if (((int)param_3 < (int)param_2) || ((int)(param_3 >> 0x20) < (int)(param_2 >> 0x20))) {
    FUN_ffff00009077de54
              (s__s__d__drawing_width_or_height_i_ffff0000907d55a7,
               s_check_rect_valid_ffff0000907df161,0x66);
    FUN_ffff00009077de54
              (s__s__d__rect_left___d__right___d__ffff0000907f76d6,
               s_check_rect_valid_ffff0000907df161,0x68,param_2 & 0xffffffff,param_3 & 0xffffffff,
               param_2 >> 0x20,param_3 >> 0x20);
    return;
  }
  iVar1 = *(int *)((long)param_5 + 0x24);
  if (param_6 == 0x20) {
    if (iVar1 == 0x20) {
      local_70 = param_5[2];
      uStack_68 = param_5[3];
      local_60 = param_5[4];
      uStack_58 = param_5[5];
      local_80 = *param_5;
      uStack_78 = param_5[1];
      FUN_ffff0000907c33f4(param_1,param_2,param_3,param_4,&local_80,0x20);
    }
    else {
      if (iVar1 != 0x10) {
        uVar2 = 400;
LAB_ffff0000907c413c:
        FUN_ffff00009077de54
                  (s__s__d__unsupported_physical_scre_ffff0000907da30d,
                   s_fill_rect_with_content_ffff0000907da2f6,uVar2);
        return;
      }
      local_70 = param_5[2];
      uStack_68 = param_5[3];
      local_60 = param_5[4];
      uStack_58 = param_5[5];
      local_80 = *param_5;
      uStack_78 = param_5[1];
      FUN_ffff0000907c3a10(param_1,param_2,param_3,param_4,&local_80);
    }
  }
  else if (iVar1 == 0x20) {
    local_70 = param_5[2];
    uStack_68 = param_5[3];
    local_60 = param_5[4];
    uStack_58 = param_5[5];
    local_80 = *param_5;
    uStack_78 = param_5[1];
    FUN_ffff0000907c36e8(param_1,param_2,param_3,param_4,&local_80,param_6);
  }
  else {
    if (iVar1 != 0x10) {
      uVar2 = 0x19b;
      goto LAB_ffff0000907c413c;
    }
    local_70 = param_5[2];
    uStack_68 = param_5[3];
    local_60 = param_5[4];
    uStack_58 = param_5[5];
    local_80 = *param_5;
    uStack_78 = param_5[1];
    FUN_ffff0000907c3c9c(param_1,param_2,param_3,param_4,&local_80);
  }
  return;
}


