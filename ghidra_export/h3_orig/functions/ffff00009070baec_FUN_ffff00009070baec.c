// ffff00009070baec  size=448  FUN_ffff00009070baec

void FUN_ffff00009070baec(void)

{
  long lVar1;
  int iVar2;
  char *pcVar3;
  long lVar4;
  undefined8 uVar5;
  long lVar6;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  long local_40;
  ulong local_38;
  undefined8 local_18;
  
  local_18 = 0;
  local_40 = 0;
  local_38 = 0;
  FUN_ffff0000907819d8(&local_38,(long)&local_18 + 4);
  if ((local_38 == 0) || (local_18._4_4_ == 0)) {
    FUN_ffff00009077de54(s_failed_to_get_pstore_mem_ffff0000907f248e);
    pcVar3 = s_failed_to_get_pstore_mem_ffff0000907e72f3;
  }
  else {
    iVar2 = FUN_ffff00009077b79c
                      (DAT_ffff000090839908,&DAT_ffff0000907ed94a,
                       local_18._4_4_ + 0xfffU & 0xfffff000,&local_40,0xc,local_38 & 0xfffff000,0,0)
    ;
    if (iVar2 == 0) {
      FUN_ffff00009077eb88(&local_18,0,4);
      lVar1 = local_40;
      lVar4 = FUN_ffff00009077d24c(0x200000);
      if (lVar4 != 0) {
        FUN_ffff00009077de54
                  (s_oem_dump_lpmsg__log_header___0x__ffff0000907cf113,lVar1 + 0x20f000,0x200000);
        local_50 = 0;
        uStack_48 = 0;
        local_60 = 0;
        uStack_58 = 0;
        local_70 = 0;
        uStack_68 = 0;
        local_80 = 0;
        uStack_78 = 0;
        FUN_ffff00009077f434(&local_80,0x40,s_DATA_08x_ffff0000907f5b03,0x200000);
        uVar5 = FUN_ffff00009077ef88(&local_80);
        FUN_ffff00009070f9dc(&local_80,uVar5);
        FUN_ffff00009077e858(lVar4,lVar1 + 0x20f008,0xff8);
        lVar6 = 0;
        do {
          FUN_ffff00009077e858(lVar4 + lVar6 + 0xff8,lVar1 + 0x210000 + lVar6,0x1000);
          lVar6 = lVar6 + 0x1000;
        } while (lVar6 != 0x1ff000);
        *(undefined1 *)(lVar4 + 0x1fffff) = 0;
        FUN_ffff00009070f9dc(lVar4,0x200000);
        FUN_ffff000090709d50(&DAT_ffff0000907df295);
        FUN_ffff00009077de54(s_lpmsg_log______s_ffff0000907d574b,lVar4);
        thunk_FUN_ffff0000907c2ad8(lVar4);
        FUN_ffff00009077bb68(DAT_ffff000090839908,local_40);
        return;
      }
      pcVar3 = s_alloc_data_buf_fail_ffff0000907f3db8;
    }
    else {
      FUN_ffff00009077de54(s__d__map_error_for_log_store_ffff0000907f24a9);
      pcVar3 = s_alloc_mem_failed__ffff0000907ed94f;
    }
  }
  FUN_ffff000090709d10(pcVar3);
  return;
}


