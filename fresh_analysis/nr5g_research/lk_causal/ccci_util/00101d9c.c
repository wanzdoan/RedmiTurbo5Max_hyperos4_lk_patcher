
void lk_tag_inf_parsing_to_args_table(void)

{
  uint *puVar1;
  uint *puVar2;
  uint uVar3;
  uint *puVar4;
  undefined8 uVar5;
  long lVar6;
  uint uVar7;
  undefined8 local_e8;
  undefined8 local_e0;
  uint local_d8 [16];
  undefined8 local_98;
  undefined8 uStack_90;
  undefined8 local_88;
  undefined8 uStack_80;
  undefined8 local_78;
  undefined8 uStack_70;
  undefined8 local_68;
  undefined8 uStack_60;
  long local_58;
  
  lVar6 = sp_el0;
  local_58 = *(long *)(lVar6 + 0x620);
  local_68 = 0;
  uStack_60 = 0;
  local_78 = 0;
  uStack_70 = 0;
  local_88 = 0;
  uStack_80 = 0;
  local_98 = 0;
  uStack_90 = 0;
  local_d8[0xe] = 0;
  local_d8[10] = 0;
  local_d8[0xb] = 0;
  local_d8[0xc] = 0;
  local_d8[0xd] = 0;
  local_d8[6] = 0;
  local_d8[7] = 0;
  local_d8[8] = 0;
  local_d8[9] = 0;
  local_d8[2] = 0;
  local_d8[3] = 0;
  local_d8[4] = 0;
  local_d8[5] = 0;
  local_e0 = 0;
  local_d8[0] = 0;
  local_d8[1] = 0;
  local_e8 = 0;
  if (s_g_lk_inf_base == 0) {
    ccci_dump_write(0,1,"[0/util]%s: s_g_lk_inf_base NULL\n","lk_tag_inf_parsing_to_args_table");
    ccci_dump_write(2,1,"[0/util]%s: s_g_lk_inf_base NULL\n","lk_tag_inf_parsing_to_args_table");
    uVar5 = 0xffffffff;
  }
  else {
    if (s_g_tag_cnt != 0) {
      uVar7 = 0;
      lVar6 = s_g_lk_inf_base;
      do {
        __memcpy_fromio(&local_e8,lVar6,0x4c);
        uVar3 = s_g_lk_info_tag_version;
        scnprintf(&local_98,0x40,&DAT_001103ff,&local_e8);
        puVar1 = local_d8 + 0xd;
        puVar4 = local_d8 + 0xc;
        if (uVar3 < 2) {
          puVar1 = local_d8 + 1;
          puVar4 = local_d8;
        }
        puVar2 = local_d8 + 0xe;
        if (uVar3 < 2) {
          puVar2 = local_d8 + 2;
        }
        uVar3 = *puVar2;
        mtk_ccci_add_new_args(&local_98,s_g_lk_inf_base + (ulong)*puVar4,*puVar1,0);
        uVar7 = uVar7 + 1;
        lVar6 = s_g_lk_inf_base + (ulong)uVar3;
      } while (uVar7 < s_g_tag_cnt);
    }
    uVar5 = 0;
  }
  lVar6 = sp_el0;
  if (*(long *)(lVar6 + 0x620) != local_58) {
                    /* WARNING: Subroutine does not return */
    __stack_chk_fail(uVar5);
  }
  return;
}

