// ffff00009074a558  size=880  FUN_ffff00009074a558

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

uint FUN_ffff00009074a558(uint param_1,uint param_2,undefined *param_3)

{
  undefined8 *puVar1;
  uint uVar2;
  int iVar3;
  uint uVar4;
  byte *pbVar5;
  undefined1 *puVar6;
  char *pcVar7;
  undefined4 *puVar8;
  ulong uVar9;
  byte local_34 [4];
  
  uVar9 = (ulong)param_1;
  local_34[0] = 0;
  if ((DAT_ffff000090877678 & 1) == 0) {
    iVar3 = FUN_ffff0000907849e0();
    if (iVar3 != 0) {
      DAT_ffff00009081ac80 = 0xffff000013260f3c;
      DAT_ffff00009081aca0 = 0xffff000013260f40;
      DAT_ffff00009081acc0 = 0xffff000013260f44;
      DAT_ffff00009081ace0 = 0xffff000013260f48;
      DAT_ffff00009081ad00 = 0xffff000013260f4c;
      DAT_ffff00009081ad20 = 0xffff000013260f50;
      DAT_ffff00009081ad40 = 0xffff000013260f54;
      DAT_ffff00009081ad60 = 0xffff000013260f58;
      DAT_ffff00009081ada0 = 0xffff000013264110;
      DAT_ffff00009081adc0 = 0xffff000013264114;
      DAT_ffff00009081ad80 = 0xffff00001326410c;
      DAT_ffff00009081ade0 = 0xffff000013264118;
      DAT_ffff00009081ae00 = 0xffff00001326411c;
      DAT_ffff00009081ae20 = 0xffff000013264120;
      DAT_ffff00009081ae40 = 0xffff000013260cc8;
      DAT_ffff00009081ae60 = 0xffff000013260ccc;
      DAT_ffff00009081ae80 = 0xffff000013260ba8;
    }
    DAT_ffff000090877678 = 1;
  }
  if (param_3 == &DAT_ffff00009081ac70) {
    if (7 < param_1) {
      return 0x1000;
    }
  }
  else {
    if (param_3 != &DAT_ffff00009081ad70) {
      return 0x1000;
    }
    if ((param_1 == 8) && ((param_2 >> 8 & 1) != 0)) {
      FUN_ffff00009077de54(s__Run_Time__SEC_LOCK____x_ffff0000907f9f23,_DAT_ffff000013260068);
      pbVar5 = (byte *)FUN_ffff000090741944(s_EFUSE_OTA_Protect_ffff0000907e4d7b);
      if (pbVar5 == (byte *)0x0) {
        FUN_ffff00009077de54(s__Run_Time__Try_to_init_env_ffff0000907f67dd);
        iVar3 = FUN_ffff000090741a20(s_EFUSE_OTA_Protect_ffff0000907e4d7b,local_34);
        if ((iVar3 != 0) ||
           (pbVar5 = (byte *)FUN_ffff000090741944(s_EFUSE_OTA_Protect_ffff0000907e4d7b),
           pbVar5 == (byte *)0x0)) {
          FUN_ffff00009077de54(s_EFUSE_ENV_corrupt_ffff0000907f9f3d);
          return 0x1000;
        }
      }
      local_34[0] = *pbVar5;
      FUN_ffff00009077de54(s_efuse_env__0x_x_ffff0000907cfa75);
      if ((local_34[0] & 1) != 0) {
        pcVar7 = s_efuse_env_said_Already_blow_Disa_ffff0000907ee44e;
        puVar8 = DAT_ffff00009081ae70;
        goto LAB_ffff00009074a81c;
      }
      local_34[0] = local_34[0] | 1;
      FUN_ffff000090741a20(s_EFUSE_OTA_Protect_ffff0000907e4d7b,local_34);
      puVar6 = (undefined1 *)FUN_ffff000090741944(s_EFUSE_OTA_Protect_ffff0000907e4d7b);
      FUN_ffff00009077de54(s__Run_Time__first_time_blow_Disab_ffff0000907d9531,*puVar6);
    }
    if (8 < param_1) {
      return 0x1000;
    }
  }
  puVar1 = (undefined8 *)(param_3 + uVar9 * 0x20);
  _DAT_ffff00001326f050 = _DAT_ffff00001326f050 & 0xfffffff0;
  uVar4 = *(uint *)*puVar1;
  uVar2 = *(uint *)(puVar1 + 1) & (uVar4 ^ 0xffffffff) & param_2;
  FUN_ffff00009077de54(s__Run_Time__Original___0x_x__Blow_ffff0000907ff3e2,uVar4,param_2,uVar2);
  if (uVar2 == 0) {
    FUN_ffff00009077de54(s__Run_Time__Already_blown__Bypass_ffff0000907f67f9);
    return 0;
  }
  if (((*(byte *)((long)puVar1 + 0xd) & 0xfe) == 2) &&
     (((*(uint *)(puVar1 + 1) & uVar4) != 0 ||
      ((*(uint *)(param_3 + uVar9 * 0x20 + 0x18) & **(uint **)(param_3 + uVar9 * 0x20 + 0x10)) != 0)
      ))) {
    FUN_ffff00009077de54(s__Run_Time__This_field_can_only_b_ffff0000907e653f);
    uVar4 = 0x4000;
    goto LAB_ffff00009074a84c;
  }
  thunk_FUN_ffff000090745ebc();
  iVar3 = FUN_ffff00009074a234();
  if (iVar3 == 0) {
    uVar4 = FUN_ffff00009074a8dc();
    if (uVar4 != 0) goto LAB_ffff00009074a84c;
    uVar4 = FUN_ffff00009074a930(*puVar1,uVar2);
    if (uVar4 != 0) {
      uVar4 = uVar4 | 0x100000;
      goto LAB_ffff00009074a84c;
    }
    FUN_ffff00009074a8dc(1);
    iVar3 = FUN_ffff00009074a304();
    if (iVar3 == 0) {
      FUN_ffff00009074a990();
      pcVar7 = s__Run_Time__Read_check_after_blow_ffff0000907f2e89;
      puVar8 = (undefined4 *)*puVar1;
LAB_ffff00009074a81c:
      FUN_ffff00009077de54(pcVar7,*puVar8);
      return 0;
    }
  }
  uVar4 = 0x20000;
LAB_ffff00009074a84c:
  FUN_ffff00009077de54(s__Run_Time__Blow_Fail__Status___0_ffff0000907d15ea,uVar4);
  iVar3 = FUN_ffff00009074a378();
  if (iVar3 != 0) {
    FUN_ffff00009074a304();
  }
  FUN_ffff00009074a8dc(1);
  return uVar4;
}


