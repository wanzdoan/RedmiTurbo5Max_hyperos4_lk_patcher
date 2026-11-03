// ffff000090771880  size=596  FUN_ffff000090771880

long FUN_ffff000090771880(undefined8 param_1,undefined8 param_2)

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
  
  lVar3 = FUN_ffff00009077f250(param_2);
  uVar1 = lVar3 + 1;
  if (uVar1 < 0x401) {
    puVar4 = (undefined1 *)0x0;
    puVar7 = auStack_45c;
  }
  else {
    puVar4 = (undefined1 *)thunk_FUN_ffff00009077d514(uVar1);
    puVar7 = puVar4;
  }
  local_58 = (char *)0x0;
  local_5c = 0;
  FUN_ffff00009077ee50(auStack_45c,0,0x400);
  thunk_FUN_ffff00009077eb20(puVar7,param_2,uVar1);
  puVar5 = (undefined1 *)thunk_FUN_ffff00009077f074(puVar7,0x3a);
  if (puVar5 == (undefined1 *)0x0) {
    FUN_ffff00009076fec8(s_ERROR___s____ffff0000907f36ba,s_ufdt_get_fixup_location_ffff0000907d1d61)
    ;
    pcVar6 = s_Missing_property_part_in___s__ffff0000907db735;
  }
  else {
    puVar8 = puVar5 + 1;
    *puVar5 = 0;
    puVar5 = (undefined1 *)thunk_FUN_ffff00009077f074(puVar8,0x3a);
    if (puVar5 != (undefined1 *)0x0) {
      *puVar5 = 0;
      uVar2 = thunk_FUN_ffff00009077d938(puVar5 + 1,&local_58,10);
      if (*local_58 == '\0') {
        if (-1 < (int)uVar2) {
          lVar3 = FUN_ffff0000907701d8(param_1,puVar7);
          if (lVar3 == 0) {
            FUN_ffff00009076fec8
                      (s_ERROR___s____ffff0000907f36ba,s_ufdt_get_fixup_location_ffff0000907d1d61);
            pcVar6 = s_Path___s__not_found_ffff0000907f7220;
          }
          else {
            lVar3 = FUN_ffff000090770f60(lVar3,puVar8,&local_5c);
            if (lVar3 == 0) {
              FUN_ffff00009076fec8
                        (s_ERROR___s____ffff0000907f36ba,s_ufdt_get_fixup_location_ffff0000907d1d61)
              ;
              FUN_ffff00009076fec8
                        (s_Property___s__not_found_in___s__n_ffff0000907fa7d1,puVar8,puVar7);
              goto joined_r0xffff000090771974;
            }
            if ((3 < local_5c) && ((int)uVar2 <= local_5c + -4)) {
              if (puVar4 != (undefined1 *)0x0) {
                thunk_FUN_ffff0000907c314c(puVar4);
              }
              return lVar3 + (ulong)uVar2;
            }
            FUN_ffff00009076fec8
                      (s_ERROR___s____ffff0000907f36ba,s_ufdt_get_fixup_location_ffff0000907d1d61);
            pcVar6 = s__s__property_length_is_too_small_ffff0000907d84d0;
          }
          goto LAB_ffff0000907719b8;
        }
        FUN_ffff00009076fec8
                  (s_ERROR___s____ffff0000907f36ba,s_ufdt_get_fixup_location_ffff0000907d1d61);
        pcVar6 = s___s__is_not_a_valid_offset_ffff0000907deb75;
      }
      else {
        FUN_ffff00009076fec8
                  (s_ERROR___s____ffff0000907f36ba,s_ufdt_get_fixup_location_ffff0000907d1d61);
        pcVar6 = s___s__is_not_a_valid_number_ffff0000907d3767;
      }
      FUN_ffff00009076fec8(pcVar6,puVar5 + 1);
      goto joined_r0xffff000090771974;
    }
    FUN_ffff00009076fec8(s_ERROR___s____ffff0000907f36ba,s_ufdt_get_fixup_location_ffff0000907d1d61)
    ;
    pcVar6 = s_Missing_offset_part_in___s__ffff0000907d00e2;
  }
LAB_ffff0000907719b8:
  FUN_ffff00009076fec8(pcVar6,puVar7);
joined_r0xffff000090771974:
  if (puVar4 != (undefined1 *)0x0) {
    thunk_FUN_ffff0000907c314c(puVar4);
  }
  return 0;
}


