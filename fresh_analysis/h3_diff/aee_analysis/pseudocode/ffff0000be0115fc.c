
void FUN_ffff0000be0115fc(char *param_1)

{
  int iVar1;
  undefined8 *puVar2;
  undefined1 *local_c0;
  undefined1 *puStack_b8;
  undefined8 local_b0;
  undefined8 uStack_a8;
  undefined8 local_a0;
  undefined8 uStack_98;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined8 local_40;
  undefined8 uStack_38;
  undefined8 local_30;
  undefined8 uStack_28;
  
  puStack_b8 = &stack0xffffffffffffff20;
  local_30 = 0;
  uStack_28 = 0;
  local_40 = 0;
  uStack_38 = 0;
  local_50 = 0;
  uStack_48 = 0;
  local_60 = 0;
  uStack_58 = 0;
  local_70 = 0;
  uStack_68 = 0;
  local_80 = 0;
  uStack_78 = 0;
  local_90 = 0;
  uStack_88 = 0;
  local_a0 = 0;
  uStack_98 = 0;
  if (*param_1 == '\r') {
    puVar2 = (undefined8 *)((ulong)&local_a0 | 1);
    param_1 = param_1 + 1;
    local_a0 = 0xd;
  }
  else {
    puVar2 = &local_a0;
  }
  *(undefined2 *)puVar2 = 0x3a45;
  local_b0 = 0;
  uStack_a8 = 0xffffffc8;
  local_c0 = (undefined1 *)register0x00000008;
  iVar1 = FUN_ffff0000be061230
                    ((undefined2 *)((long)puVar2 + 2),&stack0xffffffffffffffe0 + -((long)puVar2 + 2)
                     ,param_1,&local_c0);
  if (0 < iVar1) {
    FUN_ffff0000be034f1c(&DAT_ffff0000be0ac42b,&local_a0);
    FUN_ffff0000be05fd98(s___s___s_ffff0000be0b3dba,s_MRDUMP11_ffff0000be0a7a51,&local_a0);
    return;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000be05ee98(s_app_aee_mrdump_log_c_ffff0000be0c0459,0x35,s_err_>_0_ffff0000be0bbb4e);
}


