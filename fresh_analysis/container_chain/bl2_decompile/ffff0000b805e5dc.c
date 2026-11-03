
int FUN_ffff0000b805e5dc(long param_1,undefined8 param_2,undefined1 *param_3,long param_4)

{
  uint uVar1;
  int iVar2;
  ulong uVar3;
  long lVar4;
  uint *puVar5;
  undefined1 *puVar6;
  char *pcVar7;
  uint uVar8;
  undefined8 local_188;
  undefined8 uStack_180;
  undefined8 local_178;
  undefined8 uStack_170;
  undefined8 local_168;
  undefined8 uStack_160;
  undefined8 local_158;
  undefined8 uStack_150;
  undefined8 local_148;
  undefined8 uStack_140;
  undefined8 local_138;
  undefined8 uStack_130;
  undefined8 local_128;
  undefined8 uStack_120;
  undefined8 local_118;
  undefined8 uStack_110;
  undefined8 local_108;
  undefined8 uStack_100;
  undefined8 local_f8;
  undefined8 uStack_f0;
  undefined8 local_e8;
  undefined8 uStack_e0;
  undefined8 local_d8;
  undefined8 uStack_d0;
  undefined8 local_c8;
  undefined8 uStack_c0;
  undefined8 local_b8;
  undefined8 uStack_b0;
  uint local_a8 [18];
  
  local_a8[0xc] = 0;
  local_a8[0xd] = 0;
  local_a8[0xe] = 0;
  local_a8[0xf] = 0;
  local_a8[8] = 0;
  local_a8[9] = 0;
  local_a8[10] = 0;
  local_a8[0xb] = 0;
  local_a8[4] = 0;
  local_a8[5] = 0;
  local_a8[6] = 0;
  local_a8[7] = 0;
  local_a8[0] = 0;
  local_a8[1] = 0;
  local_a8[2] = 0;
  local_a8[3] = 0;
  local_b8 = 0;
  uStack_b0 = 0;
  local_c8 = 0;
  uStack_c0 = 0;
  local_d8 = 0;
  uStack_d0 = 0;
  local_e8 = 0;
  uStack_e0 = 0;
  local_f8 = 0;
  uStack_f0 = 0;
  local_108 = 0;
  uStack_100 = 0;
  local_118 = 0;
  uStack_110 = 0;
  local_128 = 0;
  uStack_120 = 0;
  local_138 = 0;
  uStack_130 = 0;
  local_148 = 0;
  uStack_140 = 0;
  local_158 = 0;
  uStack_150 = 0;
  local_168 = 0;
  uStack_160 = 0;
  local_178 = 0;
  uStack_170 = 0;
  local_188 = 0;
  uStack_180 = 0;
  if ((param_1 == 0) || (param_3 == (undefined1 *)0x0)) {
    FUN_ffff0000b804c33c(s_Error__wrong_input_parameter_for_ffff0000b808b289);
    iVar2 = -2;
  }
  else {
    if (param_4 != 0) {
      uVar8 = 0;
      do {
        uVar1 = (uVar8 & 0xff00ff00) >> 8 | (uVar8 & 0xff00ff) << 8;
        local_a8[0] = uVar1 >> 0x10 | uVar1 << 0x10;
        iVar2 = FUN_ffff0000b806266c(&local_188);
        pcVar7 = s_Error__fail_to_init_sha384_for_p_ffff0000b80a1759;
        if (iVar2 != 0) {
LAB_ffff0000b805e748:
          FUN_ffff0000b804c33c(pcVar7);
          goto LAB_ffff0000b805e750;
        }
        iVar2 = FUN_ffff0000b80626cc(&local_188,param_1,param_2);
        if (iVar2 != 0) {
          pcVar7 = s_Error__fail_to_process_sha384__1_ffff0000b808cc17;
          goto LAB_ffff0000b805e748;
        }
        iVar2 = FUN_ffff0000b80626cc(&local_188,local_a8,4);
        pcVar7 = s_Error__fail_to_process_sha384__2_ffff0000b808972b;
        if ((iVar2 != 0) ||
           (iVar2 = FUN_ffff0000b806275c(&local_188,local_a8),
           pcVar7 = s_Error__fail_to_finish_sha384_for_ffff0000b8086bf6, iVar2 != 0))
        goto LAB_ffff0000b805e748;
        uVar3 = param_4 - 1;
        uVar8 = uVar8 + 1;
        if (0x2e < uVar3) {
          uVar3 = 0x2f;
        }
        lVar4 = uVar3 + 1;
        puVar5 = local_a8;
        puVar6 = param_3;
        do {
          param_4 = param_4 + -1;
          lVar4 = lVar4 + -1;
          param_3 = puVar6 + 1;
          *puVar6 = (char)*puVar5;
          puVar5 = (uint *)((long)puVar5 + 1);
          puVar6 = param_3;
        } while (lVar4 != 0);
      } while (param_4 != 0);
    }
    iVar2 = 0;
LAB_ffff0000b805e750:
    FUN_ffff0000b804d070(local_a8,0,0x40);
    FUN_ffff0000b804d070(&local_188,0,0xe0);
  }
  return iVar2;
}


