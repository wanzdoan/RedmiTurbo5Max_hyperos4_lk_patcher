// ffff000090786e88  size=456  FUN_ffff000090786e88

undefined8 FUN_ffff000090786e88(undefined8 param_1,long param_2,char param_3)

{
  uint uVar1;
  undefined8 uVar2;
  char *pcVar3;
  ulong uVar4;
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
  undefined8 local_a8;
  undefined8 uStack_a0;
  undefined8 local_98;
  undefined8 uStack_90;
  undefined8 local_88;
  undefined8 uStack_80;
  undefined8 local_78;
  undefined8 uStack_70;
  undefined8 local_68;
  undefined8 uStack_60;
  undefined8 local_58;
  char local_4c [4];
  uint local_48;
  byte local_44 [4];
  
  local_44[0] = 0;
  local_48 = 0;
  if (param_2 == 0) {
    pcVar3 = s_parse_secid_failed__ffff0000907f369e;
LAB_ffff000090786fb4:
    FUN_ffff00009077de54(pcVar3);
LAB_ffff000090786fb8:
    uVar2 = 1;
  }
  else {
    local_4c[0] = '\0';
    local_118 = 0;
    uStack_110 = 0;
    local_108 = 0;
    uStack_100 = 0;
    local_f8 = 0;
    uStack_f0 = 0;
    local_e8 = 0;
    uStack_e0 = 0;
    local_d8 = 0;
    uStack_d0 = 0;
    local_c8 = 0;
    uStack_c0 = 0;
    local_b8 = 0;
    uStack_b0 = 0;
    local_a8 = 0;
    uStack_a0 = 0;
    local_98 = 0;
    uStack_90 = 0;
    local_88 = 0;
    uStack_80 = 0;
    local_78 = 0;
    uStack_70 = 0;
    local_68 = 0;
    uStack_60 = 0;
    local_58 = 0;
    FUN_ffff00009077e858(&local_48,param_2 + 10,4);
    uVar2 = 0;
    local_48 = local_48 + 0xe;
    if (0xe < local_48) {
      uVar4 = 0xe;
      do {
        if ((ulong)local_48 - 2 < uVar4) {
          FUN_ffff00009077de54
                    (s_length_error__offset__d__len__d_ffff0000907d1f03,uVar4,(ulong)local_48);
          return 5;
        }
        FUN_ffff00009077eb88(&local_118,0,200);
        FUN_ffff00009077e858(local_4c,param_2 + uVar4,1);
        if (local_4c[0] == '\0') {
          pcVar3 = s_not_find_tlv_type__ffff0000907d1f24;
          goto LAB_ffff000090786fb4;
        }
        FUN_ffff00009077e858(local_44,param_2 + (ulong)((int)uVar4 + 1),1);
        uVar1 = (int)uVar4 + 2;
        if (local_48 - uVar1 < (uint)local_44[0]) {
          FUN_ffff00009077de54
                    (s_length_error__invalid_length__le_ffff00009080163a,(uint)local_44[0]);
          goto LAB_ffff000090786fb8;
        }
        if (local_4c[0] == param_3) {
          FUN_ffff00009077e858(&local_118,param_2 + (ulong)uVar1,local_44[0]);
          uVar2 = FUN_ffff00009077ede4(&local_118,param_1);
          if ((int)uVar2 == 0) {
            return uVar2;
          }
          FUN_ffff00009077de54
                    (s_The_secure_id_and_normal_id_are_n_ffff0000907ed1ae,param_3,param_1,&local_118
                    );
          goto LAB_ffff000090786fb8;
        }
        uVar4 = (ulong)(uVar1 + local_44[0]);
      } while (uVar1 + local_44[0] < local_48);
      uVar2 = 0;
    }
  }
  return uVar2;
}


