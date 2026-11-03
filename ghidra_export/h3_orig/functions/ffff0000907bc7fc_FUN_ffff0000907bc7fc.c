// ffff0000907bc7fc  size=864  FUN_ffff0000907bc7fc

undefined8 FUN_ffff0000907bc7fc(long param_1,ulong param_2,code *param_3,undefined8 param_4)

{
  int iVar1;
  undefined8 uVar2;
  ulong uVar3;
  ulong uVar4;
  ulong uVar5;
  undefined *puVar6;
  char *pcVar7;
  ulong uVar8;
  ulong local_48;
  ulong local_28;
  
  local_28 = 0;
  if (param_1 == 0) {
    uVar2 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_descript_ffff0000907ea2ee);
    puVar6 = &DAT_ffff0000907dd665;
    pcVar7 = s_image_data_is_NULL___ffff0000907d89b8;
  }
  else if (param_3 == (code *)0x0) {
    uVar2 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_descript_ffff0000907ea2ee);
    puVar6 = &DAT_ffff0000907f096d;
    pcVar7 = s_foreach_func_is_NULL___ffff0000907e587c;
  }
  else if (param_2 < 0x100) {
    uVar2 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_descript_ffff0000907ea2ee);
    puVar6 = &DAT_ffff0000908033e3;
    pcVar7 = s_Length_is_smaller_than_header__ffff0000907d704e;
  }
  else {
    iVar1 = thunk_FUN_ffff00009077e820(param_1,&DAT_ffff0000907e3dcf,4);
    if (iVar1 == 0) {
      uVar2 = FUN_ffff0000907c09ec(*(undefined8 *)(param_1 + 0xc));
      uVar3 = FUN_ffff0000907c0bc8(&local_28,0x100,uVar2);
      if ((uVar3 & 1) == 0) {
        uVar2 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_descript_ffff0000907ea2ee);
        puVar6 = &DAT_ffff0000907df076;
        pcVar7 = s_Invalid_authentication_data_bloc_ffff0000907d706e;
      }
      else {
        uVar2 = FUN_ffff0000907c09ec(*(undefined8 *)(param_1 + 0x60));
        uVar3 = FUN_ffff0000907c0ab8(&local_28,uVar2);
        if ((uVar3 & 1) == 0) {
          uVar2 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_descript_ffff0000907ea2ee);
          puVar6 = &DAT_ffff0000907f3b82;
          pcVar7 = s_Invalid_descriptors_offset__ffff0000907da267;
        }
        else {
          if (param_2 < local_28) {
            uVar2 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_descript_ffff0000907ea2ee);
            puVar6 = &DAT_ffff0000907e0756;
          }
          else {
            uVar3 = param_1 + local_28;
            uVar4 = FUN_ffff0000907c09ec(*(undefined8 *)(param_1 + 0x68));
            if (uVar4 <= (param_1 + param_2) - uVar3) {
              if ((long)uVar4 < 1) {
                return 1;
              }
              uVar4 = uVar3 + uVar4;
              while( true ) {
                uVar8 = uVar4 - uVar3;
                local_48 = 0;
                if (uVar8 < 0x10) break;
                uVar2 = FUN_ffff0000907c09ec(*(undefined8 *)(uVar3 + 8));
                uVar5 = FUN_ffff0000907c0bc8(&local_48,0x10,uVar2);
                if ((uVar5 & 1) == 0) {
                  uVar2 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_descript_ffff0000907ea2ee);
                  puVar6 = &DAT_ffff00009080199f;
                  goto LAB_ffff0000907bcb18;
                }
                if ((local_48 & 7) != 0) {
                  uVar2 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_descript_ffff0000907ea2ee);
                  puVar6 = (undefined *)0xffff0000907fac5b;
                  goto LAB_ffff0000907bcb18;
                }
                if (uVar8 < local_48) {
                  uVar2 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_descript_ffff0000907ea2ee);
                  puVar6 = &DAT_ffff0000907d7097;
                  pcVar7 = s_Invalid_data_in_descriptors_arra_ffff0000907e7041;
                  goto LAB_ffff0000907bca18;
                }
                uVar8 = (*param_3)(uVar3,param_4);
                if ((uVar8 & 1) == 0) {
                  return 0;
                }
                uVar3 = uVar3 + local_48;
                if (uVar4 <= uVar3) {
                  return 1;
                }
              }
              uVar2 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_descript_ffff0000907ea2ee);
              puVar6 = &DAT_ffff0000907e075a;
LAB_ffff0000907bcb18:
              pcVar7 = s_Invalid_descriptor_length__ffff0000907f222f;
              goto LAB_ffff0000907bca18;
            }
            uVar2 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_descript_ffff0000907ea2ee);
            puVar6 = &DAT_ffff0000907df079;
          }
          pcVar7 = s_Descriptors_not_inside_passed_in_ffff0000907e226e;
        }
      }
    }
    else {
      uVar2 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_descript_ffff0000907ea2ee);
      puVar6 = &DAT_ffff0000907f3b7f;
      pcVar7 = s_Magic_is_incorrect__ffff0000907f755e;
    }
  }
LAB_ffff0000907bca18:
  FUN_ffff0000907c0874(uVar2,&DAT_ffff0000907ebe23,puVar6,s___ERROR__ffff0000907e0739,pcVar7,0);
  return 0;
}


