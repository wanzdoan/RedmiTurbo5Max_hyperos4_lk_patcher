
long FUN_ffff0000b8079188(undefined8 param_1,undefined8 param_2)

{
  ulong uVar1;
  uint uVar2;
  long lVar3;
  undefined1 *puVar4;
  undefined1 *puVar5;
  char *pcVar6;
  undefined1 *puVar7;
  undefined1 *puVar8;
  undefined1 auStack_45c [1024];
  int local_5c;
  char *local_58;
  
  lVar3 = FUN_ffff0000b804d3cc(param_2);
  uVar1 = lVar3 + 1;
  if (uVar1 < 0x401) {
    puVar4 = (undefined1 *)0x0;
    puVar7 = auStack_45c;
  }
  else {
    puVar4 = (undefined1 *)thunk_FUN_ffff0000b804bccc(uVar1);
    puVar7 = puVar4;
  }
  local_58 = (char *)0x0;
  local_5c = 0;
  FUN_ffff0000b804d070(auStack_45c,0,0x400);
  thunk_FUN_ffff0000b804cd40(puVar7,param_2,uVar1);
  puVar5 = (undefined1 *)thunk_FUN_ffff0000b804d210(puVar7,0x3a);
  if (puVar5 == (undefined1 *)0x0) {
    FUN_ffff0000b80777d0(s_ERROR___s____ffff0000b809976e,s_ufdt_get_fixup_location_ffff0000b8086d31)
    ;
    pcVar6 = s_Missing_property_part_in___s__ffff0000b808c1b8;
  }
  else {
    puVar8 = puVar5 + 1;
    *puVar5 = 0;
    puVar5 = (undefined1 *)thunk_FUN_ffff0000b804d210(puVar8,0x3a);
    if (puVar5 != (undefined1 *)0x0) {
      *puVar5 = 0;
      uVar2 = thunk_FUN_ffff0000b804c008(puVar5 + 1,&local_58,10);
      if (*local_58 == '\0') {
        if (-1 < (int)uVar2) {
          lVar3 = FUN_ffff0000b8077ae0(param_1,puVar7);
          if (lVar3 == 0) {
            FUN_ffff0000b80777d0
                      (s_ERROR___s____ffff0000b809976e,s_ufdt_get_fixup_location_ffff0000b8086d31);
            pcVar6 = s_Path___s__not_found_ffff0000b809b2b2;
          }
          else {
            lVar3 = FUN_ffff0000b8078868(lVar3,puVar8,&local_5c);
            if (lVar3 == 0) {
              FUN_ffff0000b80777d0
                        (s_ERROR___s____ffff0000b809976e,s_ufdt_get_fixup_location_ffff0000b8086d31)
              ;
              FUN_ffff0000b80777d0
                        (s_Property___s__not_found_in___s__n_ffff0000b809d219,puVar8,puVar7);
              goto joined_r0xffff0000b807927c;
            }
            if ((3 < local_5c) && ((int)uVar2 <= local_5c + -4)) {
              if (puVar4 != (undefined1 *)0x0) {
                thunk_FUN_ffff0000b8082558(puVar4);
              }
              return lVar3 + (ulong)uVar2;
            }
            FUN_ffff0000b80777d0
                      (s_ERROR___s____ffff0000b809976e,s_ufdt_get_fixup_location_ffff0000b8086d31);
            pcVar6 = s__s__property_length_is_too_small_ffff0000b808a5e4;
          }
          goto LAB_ffff0000b80792c0;
        }
        FUN_ffff0000b80777d0
                  (s_ERROR___s____ffff0000b809976e,s_ufdt_get_fixup_location_ffff0000b8086d31);
        pcVar6 = s___s__is_not_a_valid_offset_ffff0000b808d9ff;
      }
      else {
        FUN_ffff0000b80777d0
                  (s_ERROR___s____ffff0000b809976e,s_ufdt_get_fixup_location_ffff0000b8086d31);
        pcVar6 = s___s__is_not_a_valid_number_ffff0000b8087c92;
      }
      FUN_ffff0000b80777d0(pcVar6,puVar5 + 1);
      goto joined_r0xffff0000b807927c;
    }
    FUN_ffff0000b80777d0(s_ERROR___s____ffff0000b809976e,s_ufdt_get_fixup_location_ffff0000b8086d31)
    ;
    pcVar6 = s_Missing_offset_part_in___s__ffff0000b8085da3;
  }
LAB_ffff0000b80792c0:
  FUN_ffff0000b80777d0(pcVar6,puVar7);
joined_r0xffff0000b807927c:
  if (puVar4 != (undefined1 *)0x0) {
    thunk_FUN_ffff0000b8082558(puVar4);
  }
  return 0;
}


