
/* get_dev_sn(char*) */

void get_dev_sn(char *param_1)

{
  long lVar1;
  int iVar2;
  char *__s;
  undefined8 uVar3;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 uStack_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 uStack_50;
  undefined4 uStack_48;
  undefined4 local_44;
  undefined4 uStack_40;
  undefined8 uStack_3c;
  long local_28;

  lVar1 = tpidr_el0;
  local_28 = *(long *)(lVar1 + 0x28);
  uStack_3c = 0;
  uStack_40 = 0;
  uStack_78 = 0;
  local_80 = 0;
  uStack_68 = 0;
  uStack_70 = 0;
  uStack_58 = 0;
  local_60 = 0;
  uStack_48 = 0;
  local_44 = 0;
  uStack_50 = 0;
  uStack_88 = 0;
  local_90 = 0;
  property_get("ro.product.device",&local_90,"ooops");
  if (((int)local_90 == 0x67656d6f && local_90._4_1_ == 'a') ||
     ((int)local_90 == 0x656b696e && local_90._4_1_ == 'l')) {
    iVar2 = get_longcheer_sn(param_1);
    if (-1 < iVar2) goto LAB_0037c770;
    __s = "Error: Get longcheer\'s serial number.";
  }
  else {
    iVar2 = get_miphone_sn(param_1);
    if (-1 < iVar2) {
LAB_0037c770:
      uVar3 = 0;
      goto LAB_0037c788;
    }
    __s = "Error: Get miphone\'s serial number.";
  }
  puts(__s);
  uVar3 = 0xffffffff;
LAB_0037c788:
  if (*(long *)(lVar1 + 0x28) == local_28) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail(uVar3);
}
