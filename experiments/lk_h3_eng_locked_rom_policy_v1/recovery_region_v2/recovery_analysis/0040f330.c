
/* android::base::GetProperty(std::string const&, std::string const&) */

void __thiscall android::base::GetProperty(base *this,string *param_1,string *param_2)

{
  ulong uVar1;
  string *psVar2;
  ulong uVar3;
  long lVar4;
  long lVar5;
  string *in_x8;
  base *pbVar6;
  undefined8 uVar7;
  undefined8 uVar8;
  ulong local_50;
  ulong uStack_48;
  void *local_40;
  long local_38;

  lVar4 = tpidr_el0;
  local_38 = *(long *)(lVar4 + 0x28);
  local_50 = 0;
  uStack_48 = 0;
  pbVar6 = *(base **)(this + 0x10);
  local_40 = (void *)0x0;
  if (((byte)*this & 1) == 0) {
    pbVar6 = this + 1;
  }
  lVar5 = __system_property_find(pbVar6);
  if (lVar5 == 0) {
    if (((byte)*param_1 & 1) == 0) {
      if (*(long *)(lVar4 + 0x28) == local_38) {
        uVar8 = *(undefined8 *)param_1;
        uVar7 = *(undefined8 *)(param_1 + 0x10);
        *(undefined8 *)(in_x8 + 8) = *(undefined8 *)(param_1 + 8);
        *(undefined8 *)in_x8 = uVar8;
        *(undefined8 *)(in_x8 + 0x10) = uVar7;
        return;
      }
    }
    else if (*(long *)(lVar4 + 0x28) == local_38) {
      std::string::__init_copy_ctor_external
                (in_x8,*(char **)(param_1 + 0x10),*(ulong *)(param_1 + 8));
      return;
    }
  }
  else {
    __system_property_read_callback
              (lVar5,GetProperty(std::string_const&,std::string_const&)::$_0::__invoke,&local_50);
    uVar1 = local_50 >> 1 & 0x7f;
    if ((local_50 & 1) != 0) {
      uVar1 = uStack_48;
    }
    psVar2 = param_1;
    if (uVar1 != 0) {
      psVar2 = (string *)&local_50;
    }
    if (((byte)*psVar2 & 1) == 0) {
      uVar8 = *(undefined8 *)psVar2;
      uVar7 = *(undefined8 *)(psVar2 + 0x10);
      *(undefined8 *)(in_x8 + 8) = *(undefined8 *)(psVar2 + 8);
      *(undefined8 *)in_x8 = uVar8;
      *(undefined8 *)(in_x8 + 0x10) = uVar7;
    }
    else {
      psVar2 = param_1 + 0x10;
      if (uVar1 != 0) {
        psVar2 = (string *)&local_40;
      }
      uVar3 = *(ulong *)(param_1 + 8);
      if (uVar1 != 0) {
        uVar3 = uStack_48;
      }
      std::string::__init_copy_ctor_external(in_x8,*(char **)psVar2,uVar3);
    }
    if ((local_50 & 1) != 0) {
      operator_delete(local_40);
    }
    if (*(long *)(lVar4 + 0x28) == local_38) {
      return;
    }
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}
