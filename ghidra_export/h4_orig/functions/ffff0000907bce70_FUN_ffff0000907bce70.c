// ffff0000907bce70  size=864  FUN_ffff0000907bce70

undefined8 FUN_ffff0000907bce70(long param_1,ulong param_2,code *param_3,undefined8 param_4)

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
    uVar2 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_descript_ffff0000907ea432);
    puVar6 = &DAT_ffff0000907dd708;
    pcVar7 = s_image_data_is_NULL___ffff0000907d8a39;
  }
  else if (param_3 == (code *)0x0) {
    uVar2 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_descript_ffff0000907ea432);
    puVar6 = &DAT_ffff0000907f0ade;
    pcVar7 = s_foreach_func_is_NULL___ffff0000907e5980;
  }
  else if (param_2 < 0x100) {
    uVar2 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_descript_ffff0000907ea432);
    puVar6 = &DAT_ffff00009080360b;
    pcVar7 = s_Length_is_smaller_than_header__ffff0000907d70e4;
  }
  else {
    iVar1 = thunk_FUN_ffff00009077eae8(param_1,&DAT_ffff0000907e3ea0,4);
    if (iVar1 == 0) {
      uVar2 = FUN_ffff0000907c1060(*(undefined8 *)(param_1 + 0xc));
      uVar3 = FUN_ffff0000907c123c(&local_28,0x100,uVar2);
      if ((uVar3 & 1) == 0) {
        uVar2 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_descript_ffff0000907ea432);
        puVar6 = &DAT_ffff0000907df119;
        pcVar7 = s_Invalid_authentication_data_bloc_ffff0000907d7104;
      }
      else {
        uVar2 = FUN_ffff0000907c1060(*(undefined8 *)(param_1 + 0x60));
        uVar3 = FUN_ffff0000907c112c(&local_28,uVar2);
        if ((uVar3 & 1) == 0) {
          uVar2 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_descript_ffff0000907ea432);
          puVar6 = &DAT_ffff0000907f3d0e;
          pcVar7 = s_Invalid_descriptors_offset__ffff0000907da2fe;
        }
        else {
          if (param_2 < local_28) {
            uVar2 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_descript_ffff0000907ea432);
            puVar6 = &DAT_ffff0000907e07f9;
          }
          else {
            uVar3 = param_1 + local_28;
            uVar4 = FUN_ffff0000907c1060(*(undefined8 *)(param_1 + 0x68));
            if (uVar4 <= (param_1 + param_2) - uVar3) {
              if ((long)uVar4 < 1) {
                return 1;
              }
              uVar4 = uVar3 + uVar4;
              while( true ) {
                uVar8 = uVar4 - uVar3;
                local_48 = 0;
                if (uVar8 < 0x10) break;
                uVar2 = FUN_ffff0000907c1060(*(undefined8 *)(uVar3 + 8));
                uVar5 = FUN_ffff0000907c123c(&local_48,0x10,uVar2);
                if ((uVar5 & 1) == 0) {
                  uVar2 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_descript_ffff0000907ea432);
                  puVar6 = &DAT_ffff000090801b7b;
                  goto LAB_ffff0000907bd18c;
                }
                if ((local_48 & 7) != 0) {
                  uVar2 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_descript_ffff0000907ea432);
                  puVar6 = (undefined *)0xffff0000907fadf7;
                  goto LAB_ffff0000907bd18c;
                }
                if (uVar8 < local_48) {
                  uVar2 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_descript_ffff0000907ea432);
                  puVar6 = &DAT_ffff0000907d712d;
                  pcVar7 = s_Invalid_data_in_descriptors_arra_ffff0000907e7185;
                  goto LAB_ffff0000907bd08c;
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
              uVar2 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_descript_ffff0000907ea432);
              puVar6 = &DAT_ffff0000907e07fd;
LAB_ffff0000907bd18c:
              pcVar7 = s_Invalid_descriptor_length__ffff0000907f23bb;
              goto LAB_ffff0000907bd08c;
            }
            uVar2 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_descript_ffff0000907ea432);
            puVar6 = &DAT_ffff0000907df11c;
          }
          pcVar7 = s_Descriptors_not_inside_passed_in_ffff0000907e233f;
        }
      }
    }
    else {
      uVar2 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_descript_ffff0000907ea432);
      puVar6 = &DAT_ffff0000907f3d0b;
      pcVar7 = s_Magic_is_incorrect__ffff0000907f76e8;
    }
  }
LAB_ffff0000907bd08c:
  FUN_ffff0000907c0ee8(uVar2,&DAT_ffff0000907ebfa6,puVar6,s___ERROR__ffff0000907e07dc,pcVar7,0);
  return 0;
}


