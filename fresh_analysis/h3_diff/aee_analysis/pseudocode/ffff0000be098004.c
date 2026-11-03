
long FUN_ffff0000be098004(undefined8 param_1,undefined8 param_2)

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
  
  lVar3 = FUN_ffff0000be060e5c(param_2);
  uVar1 = lVar3 + 1;
  if (uVar1 < 0x401) {
    puVar4 = (undefined1 *)0x0;
    puVar7 = auStack_45c;
  }
  else {
    puVar4 = (undefined1 *)thunk_FUN_ffff0000be05f1bc(uVar1);
    puVar7 = puVar4;
  }
  local_58 = (char *)0x0;
  local_5c = 0;
  FUN_ffff0000be060ae0(auStack_45c,0,0x400);
  thunk_FUN_ffff0000be0607b0(puVar7,param_2,uVar1);
  puVar5 = (undefined1 *)thunk_FUN_ffff0000be060c80(puVar7,0x3a);
  if (puVar5 == (undefined1 *)0x0) {
    FUN_ffff0000be09664c(s_ERROR___s____ffff0000be0bcb11,s_ufdt_get_fixup_location_ffff0000be0a53d0)
    ;
    pcVar6 = s_Missing_property_part_in___s__ffff0000be0ac1ef;
  }
  else {
    puVar8 = puVar5 + 1;
    *puVar5 = 0;
    puVar5 = (undefined1 *)thunk_FUN_ffff0000be060c80(puVar8,0x3a);
    if (puVar5 != (undefined1 *)0x0) {
      *puVar5 = 0;
      uVar2 = thunk_FUN_ffff0000be05f5e0(puVar5 + 1,&local_58,10);
      if (*local_58 == '\0') {
        if (-1 < (int)uVar2) {
          lVar3 = FUN_ffff0000be09695c(param_1,puVar7);
          if (lVar3 == 0) {
            FUN_ffff0000be09664c
                      (s_ERROR___s____ffff0000be0bcb11,s_ufdt_get_fixup_location_ffff0000be0a53d0);
            pcVar6 = s_Path___s__not_found_ffff0000be0bf032;
          }
          else {
            lVar3 = FUN_ffff0000be0976e4(lVar3,puVar8,&local_5c);
            if (lVar3 == 0) {
              FUN_ffff0000be09664c
                        (s_ERROR___s____ffff0000be0bcb11,s_ufdt_get_fixup_location_ffff0000be0a53d0)
              ;
              FUN_ffff0000be09664c
                        (s_Property___s__not_found_in___s__n_ffff0000be0c15f4,puVar8,puVar7);
              goto joined_r0xffff0000be0980f8;
            }
            if ((3 < local_5c) && ((int)uVar2 <= local_5c + -4)) {
              if (puVar4 != (undefined1 *)0x0) {
                thunk_FUN_ffff0000be09937c(puVar4);
              }
              return lVar3 + (ulong)uVar2;
            }
            FUN_ffff0000be09664c
                      (s_ERROR___s____ffff0000be0bcb11,s_ufdt_get_fixup_location_ffff0000be0a53d0);
            pcVar6 = s__s__property_length_is_too_small_ffff0000be0a9d49;
          }
          goto LAB_ffff0000be09813c;
        }
        FUN_ffff0000be09664c
                  (s_ERROR___s____ffff0000be0bcb11,s_ufdt_get_fixup_location_ffff0000be0a53d0);
        pcVar6 = s___s__is_not_a_valid_offset_ffff0000be0ae20b;
      }
      else {
        FUN_ffff0000be09664c
                  (s_ERROR___s____ffff0000be0bcb11,s_ufdt_get_fixup_location_ffff0000be0a53d0);
        pcVar6 = s___s__is_not_a_valid_number_ffff0000be0a66f7;
      }
      FUN_ffff0000be09664c(pcVar6,puVar5 + 1);
      goto joined_r0xffff0000be0980f8;
    }
    FUN_ffff0000be09664c(s_ERROR___s____ffff0000be0bcb11,s_ufdt_get_fixup_location_ffff0000be0a53d0)
    ;
    pcVar6 = s_Missing_offset_part_in___s__ffff0000be0a3f9c;
  }
LAB_ffff0000be09813c:
  FUN_ffff0000be09664c(pcVar6,puVar7);
joined_r0xffff0000be0980f8:
  if (puVar4 != (undefined1 *)0x0) {
    thunk_FUN_ffff0000be09937c(puVar4);
  }
  return 0;
}


