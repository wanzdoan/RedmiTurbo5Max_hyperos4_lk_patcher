
/* get_device(int, std::string const&) */

void get_device(int param_1,string *param_2)

{
  char *__src;
  ulong uVar1;
  long lVar2;
  bool bVar3;
  int iVar4;
  long lVar5;
  string *psVar6;
  string local_138;
  char local_137 [8];
  char acStack_12f [5];
  undefined2 uStack_12a;
  char cStack_128;
  char acStack_127 [5];
  undefined1 uStack_122;
  undefined1 uStack_121;
  long local_120 [9];
  undefined4 uStack_d8;
  undefined4 local_d4;
  undefined4 uStack_d0;
  ulong uStack_cc;
  byte local_c0 [16];
  char *local_b0;
  undefined8 uStack_a8;
  undefined8 uStack_a0;
  undefined8 uStack_98;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 uStack_80;
  undefined4 uStack_78;
  undefined4 local_74;
  undefined4 uStack_70;
  undefined8 uStack_6c;
  long local_58;

  lVar2 = tpidr_el0;
  local_58 = *(long *)(lVar2 + 0x28);
  uStack_cc = 0;
  uStack_d0 = 0;
  local_120[7] = 0;
  local_120[6] = 0;
  uStack_d8 = 0;
  local_d4 = 0;
  local_120[8] = 0;
  local_120[3] = 0;
  local_120[2] = 0;
  local_120[5] = 0;
  local_120[4] = 0;
  local_120[1] = 0;
  local_120[0] = 0;
  property_get("ro.product.mod_device",local_120,"unknown");
  if (local_120[0] == 0x6e776f6e6b6e75) {
    uStack_122 = 0;
    local_138 = (string)0x2a;
    acStack_12f[0] = s_ro_product_mod_device_001554de[8];
    acStack_12f[1] = s_ro_product_mod_device_001554de[9];
    acStack_12f[2] = s_ro_product_mod_device_001554de[10];
    acStack_12f[3] = s_ro_product_mod_device_001554de[0xb];
    acStack_12f[4] = s_ro_product_mod_device_001554de[0xc];
    local_137[0] = s_ro_product_mod_device_001554de[0];
    local_137[1] = s_ro_product_mod_device_001554de[1];
    local_137[2] = s_ro_product_mod_device_001554de[2];
    local_137[3] = s_ro_product_mod_device_001554de[3];
    local_137[4] = s_ro_product_mod_device_001554de[4];
    local_137[5] = s_ro_product_mod_device_001554de[5];
    local_137[6] = s_ro_product_mod_device_001554de[6];
    local_137[7] = s_ro_product_mod_device_001554de[7];
    uStack_12a = (undefined2)s_ro_product_mod_device_001554de._13_3_;
    cStack_128 = SUB31(s_ro_product_mod_device_001554de._13_3_,2);
    acStack_127[0] = s_ro_product_mod_device_001554de[0x10];
    acStack_127[1] = s_ro_product_mod_device_001554de[0x11];
    acStack_127[2] = s_ro_product_mod_device_001554de[0x12];
    acStack_127[3] = s_ro_product_mod_device_001554de[0x13];
    acStack_127[4] = s_ro_product_mod_device_001554de[0x14];
    GetPropertyFromMiextPartition(&local_138);
    __src = (char *)((ulong)local_c0 | 1);
    if ((local_c0[0] & 1) != 0) {
      __src = local_b0;
    }
    strncpy((char *)local_120,__src,0x5c);
    if ((local_c0[0] & 1) != 0) {
      operator_delete(local_b0);
    }
    if (((byte)local_138 & 1) != 0) {
      operator_delete((void *)CONCAT17(uStack_121,
                                       CONCAT16(uStack_122,CONCAT51(acStack_127,cStack_128))));
    }
    uStack_cc = uStack_cc & 0xffffffffffffff;
    if (local_120[0] == 0x6e776f6e6b6e75) {
      bVar3 = ((byte)*param_2 & 1) != 0;
      uVar1 = (ulong)((byte)*param_2 >> 1);
      if (bVar3) {
        uVar1 = *(ulong *)(param_2 + 8);
      }
      if (uVar1 != 0) {
        psVar6 = *(string **)(param_2 + 0x10);
        if (!bVar3) {
          psVar6 = param_2 + 1;
        }
        printf("get_update_info arguments: %s\n",psVar6);
      }
      uStack_6c = 0;
      uStack_70 = 0;
      uStack_a8 = 0;
      local_b0 = (char *)0x0;
      uStack_98 = 0;
      uStack_a0 = 0;
      uStack_88 = 0;
      local_90 = 0;
      uStack_78 = 0;
      local_74 = 0;
      uStack_80 = 0;
      local_c0[8] = 0;
      local_c0[9] = 0;
      local_c0[10] = 0;
      local_c0[0xb] = 0;
      local_c0[0xc] = 0;
      local_c0[0xd] = 0;
      local_c0[0xe] = 0;
      local_c0[0xf] = 0;
      local_c0[0] = 0;
      local_c0[1] = 0;
      local_c0[2] = 0;
      local_c0[3] = 0;
      local_c0[4] = 0;
      local_c0[5] = 0;
      local_c0[6] = 0;
      local_c0[7] = 0;
      property_get("ro.product.device",local_c0,"unknown");
      lVar5 = __strlen_chk(local_c0,0x5c);
      local_c0[lVar5] = 10;
      WriteFdExactly(param_1,local_c0);
      close(param_1);
      iVar4 = puts("can not get ro.product.mod_device.");
      goto LAB_00242260;
    }
  }
  bVar3 = ((byte)*param_2 & 1) != 0;
  uVar1 = (ulong)((byte)*param_2 >> 1);
  if (bVar3) {
    uVar1 = *(ulong *)(param_2 + 8);
  }
  if (uVar1 != 0) {
    psVar6 = *(string **)(param_2 + 0x10);
    if (!bVar3) {
      psVar6 = param_2 + 1;
    }
    printf("get_update_info arguments: %s\n",psVar6);
  }
  uStack_6c = 0;
  uStack_70 = 0;
  uStack_a8 = 0;
  local_b0 = (char *)0x0;
  uStack_98 = 0;
  uStack_a0 = 0;
  uStack_88 = 0;
  local_90 = 0;
  uStack_78 = 0;
  local_74 = 0;
  uStack_80 = 0;
  local_c0[8] = 0;
  local_c0[9] = 0;
  local_c0[10] = 0;
  local_c0[0xb] = 0;
  local_c0[0xc] = 0;
  local_c0[0xd] = 0;
  local_c0[0xe] = 0;
  local_c0[0xf] = 0;
  local_c0[0] = 0;
  local_c0[1] = 0;
  local_c0[2] = 0;
  local_c0[3] = 0;
  local_c0[4] = 0;
  local_c0[5] = 0;
  local_c0[6] = 0;
  local_c0[7] = 0;
  property_get("ro.product.mod_device",local_c0,"unknown");
  lVar5 = __strlen_chk(local_c0,0x5c);
  local_c0[lVar5] = 10;
  WriteFdExactly(param_1,local_c0);
  iVar4 = close(param_1);
LAB_00242260:
  if (*(long *)(lVar2 + 0x28) == local_58) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail(iVar4);
}
